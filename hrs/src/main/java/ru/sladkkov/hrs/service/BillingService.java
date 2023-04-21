package ru.sladkkov.hrs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.enums.TypeCall;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.repository.AbonentRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {
    private final AbonentRepository abonentRepository;
    private final DataParserService dataParserService;

    @EventListener(ApplicationReadyEvent.class)
    public void calculateAllCalls() {
        for (CallDataRecordPlusDto callDataRecordPlusDto : dataParserService.getCallDataRecordDtoFromFile()) {
            var bigDecimal = calculatePriceCall(callDataRecordPlusDto);
            log.info(bigDecimal + callDataRecordPlusDto.toString());
        }
    }

    public BigDecimal calculatePriceCall(CallDataRecordPlusDto callDataRecordPlusDto) {
        var tariff = callDataRecordPlusDto.getTariff();
        var callDataRecordDto = callDataRecordPlusDto.getCallDataRecordDto();

        var abonentByTelephoneNumber = abonentRepository
                .findAbonentByAbonentNumber(callDataRecordDto.getAbonentNumber())
                .orElseThrow(() -> new AbonentNotFoundException("Такого абонента не существует"));

        long countMinuteByTariffPeriod = abonentByTelephoneNumber.getCountMinuteByTariffPeriod();

        var totalDuration = calculateDurationCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());

        countMinuteByTariffPeriod += totalDuration.toMinutes();


        if (tariff.getIncomingFree() && callDataRecordDto.getTypeCall() == TypeCall.INCOMING) {
            return BigDecimal.ZERO;
        }

        if (tariff.getOutgoingFree() && callDataRecordDto.getTypeCall() == TypeCall.OUTGOING) {
            return BigDecimal.ZERO;
        }

        if (tariff.getFreeMinuteForFixedPrice() == 0 && tariff.getActionMinute() == 0) {

            return BigDecimal.valueOf(totalDuration.toMinutes()).multiply(tariff.getPriceForMinute());
        }

        //TODO сделать звонки внутри сети

        if (totalDuration.toMinutes() > tariff.getFreeMinuteForFixedPrice()) {

            var val = tariff.getFreeMinuteForFixedPrice() - (countMinuteByTariffPeriod - totalDuration.toMinutes());

            return tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val));
        }

        var val = tariff.getActionMinute() - (countMinuteByTariffPeriod - totalDuration.toMinutes());

        if (totalDuration.toMinutes() > tariff.getActionMinute()) {

            if (val > 0) {
                return tariff.getActionPrice()
                        .multiply(BigDecimal.valueOf(val))
                        .add(tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val)));

            }

            return tariff.getPriceForMinute().multiply(BigDecimal.valueOf(totalDuration.toMinutes()));
        }

        throw new UnsupportedOperationException();
    }

    private Duration calculateDurationCall(LocalDateTime dateAndTimeStartCall, LocalDateTime dateAndTimeEndCall) {

        return Duration.between(dateAndTimeStartCall, dateAndTimeEndCall)
                .truncatedTo(ChronoUnit.MINUTES)
                .plusMinutes(1);
    }
}
