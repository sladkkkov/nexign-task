package ru.sladkkov.cdr.service.datagenerator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.exception.TariffIsEmptyException;
import ru.sladkkov.cdr.service.datagenerator.TariffGenerator;
import ru.sladkkov.common.model.Tariff;
import ru.sladkkov.common.repository.TariffRepository;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс генерации случайного тарифа.
 *
 * @author Danila Sladkov
 */
@Component
@RequiredArgsConstructor
public class TariffGeneratorImpl implements TariffGenerator {

    /**
     * Поле - репозиторий для Tariff
     */
    private final TariffRepository tariffRepository;

    /**
     * Метод получения случайного тарифа.
     *
     * @return возвращает случайно выбранный тариф типа {@link Tariff}.
     * @throws TariffIsEmptyException если таблица тарифов в базе данных пустая.
     */
    @Override
    public Tariff getRandomTariff() {

        var tariffs = tariffRepository.findAll();

        if (tariffs.isEmpty()) {
            throw new TariffIsEmptyException("Tariff table is empty");
        }

        return tariffs.get(ThreadLocalRandom.current().nextInt(tariffs.size()));

    }
}
