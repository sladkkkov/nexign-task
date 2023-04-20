package ru.sladkkov.cdr.service.datagenerator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.exception.TariffIsEmptyException;
import ru.sladkkov.cdr.service.datagenerator.TariffGenerator;
import ru.sladkkov.common.model.Tariff;
import ru.sladkkov.common.repository.TariffRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class TariffGeneratorImpl implements TariffGenerator {

    private final TariffRepository tariffRepository;
    private List<Tariff> tariffs;

    @PostConstruct
    private void init() {
        tariffs = tariffRepository.findAll();
    }

    @Override
    public Tariff generateTariff() {

        if (tariffs.isEmpty()) {
            throw new TariffIsEmptyException("Tariff table is empty");
        }

        return tariffs.get(ThreadLocalRandom.current().nextInt(tariffs.size()));

    }
}
