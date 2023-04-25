package ru.sladkkov.hrs.tariff.factory.impl;

import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.hrs.tariff.factory.Tariffable;

import java.math.BigDecimal;

public class XTariff implements Tariffable {
    @Override
    public BigDecimal calculateCallPrice(CallDataRecordDto callDataRecordDto, Long sumTotalDuration) {
        return BigDecimal.ZERO;
    }
}
