package ru.sladkkov.common.service;

import org.springframework.stereotype.Service;
import ru.sladkkov.common.model.Tariff;

@Service
public interface TariffService {
    Tariff findByTariffId(String tariffId);
}
