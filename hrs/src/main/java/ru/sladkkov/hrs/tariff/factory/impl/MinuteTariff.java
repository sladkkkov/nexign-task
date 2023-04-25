package ru.sladkkov.hrs.tariff.factory.impl;

import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.hrs.tariff.factory.Tariffable;

import java.math.BigDecimal;
import java.time.Duration;

public class MinuteTariff implements Tariffable {

    private static final BigDecimal priceForMinute = BigDecimal.valueOf(1.5);

    @Override
    public BigDecimal calculateCallPrice(CallDataRecordDto callDataRecordDto, Long sumTotalDuration) {

        Duration totalDuration = calculateDurationForOneCall(callDataRecordDto.getDateAndTimeStartCall(), callDataRecordDto.getDateAndTimeEndCall());

        return BigDecimal.valueOf(totalDuration.toMinutes()).multiply(priceForMinute);
    }

}
