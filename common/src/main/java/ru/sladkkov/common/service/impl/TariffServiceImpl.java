package ru.sladkkov.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.exception.TariffNotExistsException;
import ru.sladkkov.common.model.Tariff;
import ru.sladkkov.common.repository.TariffRepository;
import ru.sladkkov.common.service.TariffService;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

    @Override
    public Tariff findTariffById(Long id) {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new TariffNotExistsException("Такого тарифа не существует"));
    }
}
