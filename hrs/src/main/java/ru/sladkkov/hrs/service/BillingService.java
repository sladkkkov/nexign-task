package ru.sladkkov.hrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.repository.AbonentRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final KafkaTemplate<String, CallInfoDto> kafkaTemplate;
    private final AbonentRepository abonentRepository;
    private final DataParserService dataParserService;

    @KafkaListener(groupId = "hrs-topic-1", topics = "hrs-topic")
    public BigDecimal calculatePriceCall(CallDataRecordPlusDto callDataRecordPlusDto) {
        var tariff = callDataRecordPlusDto.getTariff();
        var callDataRecordDto = callDataRecordPlusDto.getCallDataRecordDto();

        var abonentByTelephoneNumber = abonentRepository
                .findByAbonentNumber(callDataRecordDto.getAbonentNumber())
                .orElseThrow(() -> new AbonentNotFoundException("Такого абонента не существует"));

        long countMinuteByTariffPeriod = abonentByTelephoneNumber.getCountMinuteByTariffPeriod();

        var totalDuration = calculateDurationCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());

        countMinuteByTariffPeriod += totalDuration.toMinutes();


        if (Boolean.TRUE.equals(tariff.getIncomingFree()) && callDataRecordDto.getTypeCall().getCode().equals("02")) {
            sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
            return BigDecimal.ZERO;
        }

        if (Boolean.TRUE.equals(tariff.getOutgoingFree()) && callDataRecordDto.getTypeCall().getCode().equals("01")) {
            sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
            return BigDecimal.ZERO;
        }

        if (tariff.getFreeMinuteForFixedPrice() == 0 && tariff.getActionMinute() == 0) {
            var cost = BigDecimal.valueOf(totalDuration.toMinutes()).multiply(tariff.getPriceForMinute());
            sendResult(callDataRecordDto, totalDuration, cost);
            return cost;
        }

        //TODO сделать звонки внутри сети

        if (totalDuration.toMinutes() > tariff.getFreeMinuteForFixedPrice()) {

            var val = tariff.getFreeMinuteForFixedPrice() - (countMinuteByTariffPeriod - totalDuration.toMinutes());
            var cost = tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val));

            sendResult(callDataRecordDto, totalDuration, cost);
            return cost;
        }

        var val = tariff.getActionMinute() - (countMinuteByTariffPeriod - totalDuration.toMinutes());

        if (totalDuration.toMinutes() > tariff.getActionMinute()) {

            if (val > 0) {

                var cost = tariff.getActionPrice()
                        .multiply(BigDecimal.valueOf(val))
                        .add(tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val)));

                sendResult(callDataRecordDto, totalDuration, cost);
                return cost;

            }
            var cost = tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes()));
            sendResult(callDataRecordDto, totalDuration, cost);
            return cost;
        }

        throw new UnsupportedOperationException();
    }

    private void sendResult(CallDataRecordDto callDataRecordDto, Duration totalDuration, BigDecimal cost) {

        String timeInHHMMSS = getDurationToStringFormat(totalDuration);

        kafkaTemplate.send("brt-db-topic", CallInfoDto.builder()
                .typeCall(callDataRecordDto.getTypeCall())
                .cost(cost)
                .startTime(callDataRecordDto.getDateAndTimeStartCall())
                .endTime(callDataRecordDto.getDateAndTimeEndCall())
                .duration(timeInHHMMSS)
                .build());
    }

    private String getDurationToStringFormat(Duration totalDuration) {
        long hh = totalDuration.toHours();
        long mm = totalDuration.toMinutesPart();
        long ss = totalDuration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    private Duration calculateDurationCall(LocalDateTime dateAndTimeStartCall, LocalDateTime dateAndTimeEndCall) {

        return Duration.between(dateAndTimeStartCall, dateAndTimeEndCall)
                .truncatedTo(ChronoUnit.MINUTES)
                .plusMinutes(1);
    }
}
