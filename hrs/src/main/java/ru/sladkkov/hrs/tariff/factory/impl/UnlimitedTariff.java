package ru.sladkkov.hrs.tariff.factory.impl;

import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.hrs.tariff.factory.Tariffable;

import java.math.BigDecimal;
import java.time.Duration;

public class UnlimitedTariff implements Tariffable {
    private static final BigDecimal MINUTE_PRICE = BigDecimal.valueOf(1);
    private static final long FIXED_COUNT_MINUTE = 100;

    @Override
    public BigDecimal calculateCallPrice(CallDataRecordDto callDataRecordDto, Long sumTotalDuration) {

        Duration totalDuration = calculateDurationForOneCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());

        sumTotalDuration += totalDuration.toMinutes();

        if (sumTotalDuration > FIXED_COUNT_MINUTE) {

            var val = FIXED_COUNT_MINUTE - (sumTotalDuration - totalDuration.toMinutes());

            return MINUTE_PRICE.multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val));

        }

        return BigDecimal.ZERO;
    }

}
