package ru.sladkkov.hrs.tariff.factory.impl;


import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.hrs.tariff.factory.Tariffable;

import java.math.BigDecimal;
import java.time.Duration;

public class OrdinaryTariff implements Tariffable {

    private static final BigDecimal PRICE_FOR_MINUTE = BigDecimal.valueOf(1.5);
    private static final BigDecimal ORDINARY_PRICE = BigDecimal.valueOf(0.5);
    private static final long FIXED_COUNT_MINUTE = 100;

    @Override
    public BigDecimal calculateCallPrice(CallDataRecordDto callDataRecordDto, Long sumTotalDuration) {

        if (callDataRecordDto.getTypeCall().getCode().equals("02")) {

            return BigDecimal.ZERO;
        }

        Duration totalDuration = calculateDurationForOneCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());

        sumTotalDuration += totalDuration.toMinutes();

        var val = FIXED_COUNT_MINUTE - (sumTotalDuration - totalDuration.toMinutes());
        if (sumTotalDuration > FIXED_COUNT_MINUTE) {

            if (val > 0) {
                return ORDINARY_PRICE
                        .multiply(BigDecimal.valueOf(val))
                        .add(PRICE_FOR_MINUTE.multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val)));

            }

            return PRICE_FOR_MINUTE.multiply(BigDecimal.valueOf(totalDuration.toMinutes()));
        }

        return ORDINARY_PRICE.multiply(BigDecimal.valueOf(totalDuration.toMinutes()));
    }

}
