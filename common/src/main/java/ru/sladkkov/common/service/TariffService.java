package ru.sladkkov.common.service;

import ru.sladkkov.common.model.Tariff;

public interface TariffService {
    Tariff findByTariffId(String tariffId);
}
