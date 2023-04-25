package ru.sladkkov.hrs.tariff.factory.impl;


import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.hrs.tariff.factory.Tariffable;

import java.math.BigDecimal;
import java.time.Duration;

public class OrdinaryTariff implements Tariffable {

    private static final BigDecimal priceForMinute = BigDecimal.valueOf(1.5);
    private static final BigDecimal ordinaryPrice = BigDecimal.valueOf(0.5);
    private static final long fixedCountMinute = 100;

    @Override
    public BigDecimal calculateCallPrice(CallDataRecordDto callDataRecordDto, Long sumTotalDuration) {


        if (callDataRecordDto.getTypeCall().getCode().equals("02")) {

            return BigDecimal.ZERO;
        }

        Duration totalDuration = calculateDurationForOneCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());

        sumTotalDuration += totalDuration.toMinutes();

        var val = fixedCountMinute - (sumTotalDuration - totalDuration.toMinutes());
        if (sumTotalDuration > fixedCountMinute) {

            if (val > 0) {
                return ordinaryPrice
                        .multiply(BigDecimal.valueOf(val))
                        .add(priceForMinute.multiply(BigDecimal.valueOf(totalDuration.toMinutes() - val)));

            }

            return priceForMinute.multiply(BigDecimal.valueOf(totalDuration.toMinutes()));
        }

        return ordinaryPrice.multiply(BigDecimal.valueOf(totalDuration.toMinutes()));
    }

}
