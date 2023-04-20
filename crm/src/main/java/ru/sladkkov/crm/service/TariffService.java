package ru.sladkkov.crm.service;

import org.springframework.stereotype.Service;
import ru.sladkkov.common.model.Tariff;

@Service
public interface TariffService {
    Tariff findTariffById(Long id);
}
