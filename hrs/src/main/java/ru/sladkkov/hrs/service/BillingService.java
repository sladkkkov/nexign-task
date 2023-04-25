package ru.sladkkov.hrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.hrs.tariff.factory.TariffFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final AbonentRepository abonentRepository;

    @KafkaListener(groupId = "hrs-topic-default", topics = "hrs-topic", containerFactory = "kafkaListenerContainerFactory")
    @SendTo("brt-reply-topic")
    public CallInfoDto calculatePriceCall(CallDataRecordPlusDto callDataRecordPlusDto) {
        var callDataRecordDto = callDataRecordPlusDto.getCallDataRecordDto();

        var tariffId = callDataRecordPlusDto.getTariff();
        var tariffByTariffId = TariffFactory.createCdr(tariffId.getTariffId());


        var totalDuration = calculateDurationCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());


        var cost = tariffByTariffId.calculateCallPrice(callDataRecordDto, getTotalDuration(callDataRecordDto));

        return sendResult(callDataRecordDto, totalDuration, cost);


    /*    var tariff = callDataRecordPlusDto.getTariff();
        var callDataRecordDto = callDataRecordPlusDto.getCallDataRecordDto();

        var abonentByTelephoneNumber = abonentRepository
                .findByAbonentNumber(callDataRecordDto.getAbonentNumber())
                .orElseThrow(() -> new AbonentNotFoundException("Такого абонента не существует"));

        long countMinuteByTariffPeriod = abonentByTelephoneNumber.getCountMinuteByTariffPeriod();


        countMinuteByTariffPeriod += totalDuration.toMinutes();


        if (Boolean.TRUE.equals(tariff.getIncomingFree()) && callDataRecordDto.getTypeCall().getCode().equals("02")) {
            sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
            return sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
        }

        if (Boolean.TRUE.equals(tariff.getOutgoingFree()) && callDataRecordDto.getTypeCall().getCode().equals("01")) {
            sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
            return sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
        }

        if (tariff.getFreeMinuteForFixedPrice() == 0 && tariff.getActionMinute() == 0) {
            var cost = BigDecimal.valueOf(totalDuration.toMinutes()).multiply(tariff.getPriceForMinute());
            sendResult(callDataRecordDto, totalDuration, cost);
            return sendResult(callDataRecordDto, totalDuration, cost);
        }

        if (totalDuration.toMinutes() > tariff.getFreeMinuteForFixedPrice() && tariff.getFreeMinuteForFixedPrice() != 0) {

            var val = tariff.getFreeMinuteForFixedPrice() - (countMinuteByTariffPeriod - totalDuration.toMinutes());
            var cost = tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val));

            return sendResult(callDataRecordDto, totalDuration, cost);
        }

        if (tariff.getFreeMinuteForFixedPrice() != 0) {
            return sendResult(callDataRecordDto, totalDuration, BigDecimal.ZERO);
        }

        var val = tariff.getActionMinute() - (countMinuteByTariffPeriod - totalDuration.toMinutes());

        if (totalDuration.toMinutes() > tariff.getActionMinute()) {

            if (val > 0) {

                var cost = tariff.getActionPrice()
                        .multiply(BigDecimal.valueOf(val))
                        .add(tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val)));

                return sendResult(callDataRecordDto, totalDuration, cost);


            }
            var cost = tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes()));

            return sendResult(callDataRecordDto, totalDuration, cost);

        }

        throw new UnsupportedOperationException();*/
    }

    private long getTotalDuration(CallDataRecordDto callDataRecordDto) {
        var abonentByTelephoneNumber = abonentRepository
                .findByAbonentNumber(callDataRecordDto.getAbonentNumber())
                .orElseThrow(() -> new AbonentNotFoundException("Такого абонента не существует"));

        return abonentByTelephoneNumber.getCountMinuteByTariffPeriod();
    }

    private CallInfoDto sendResult(CallDataRecordDto callDataRecordDto, Duration totalDuration, BigDecimal cost) {

        String timeInHHMMSS = getDurationToStringFormat(totalDuration);

        return CallInfoDto.builder()
                .typeCall(callDataRecordDto.getTypeCall())
                .cost(cost)
                .startTime(callDataRecordDto.getDateAndTimeStartCall())
                .endTime(callDataRecordDto.getDateAndTimeEndCall())
                .duration(timeInHHMMSS)
                .build();
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
