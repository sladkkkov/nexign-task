package ru.sladkkov.cdr.service.datagenerator.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.DateGenerator;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс генерации даты.
 *
 * @author Danila Sladkov
 */
@Component
public class DateGeneratorImpl implements DateGenerator {

    /**
     * Поле - максимальная длительность звонка, которая может быть сгенерированна.
     */
    @Value("${call.duration.max}")
    private Integer maxDurationInMinute;

    /**
     * Метод получения значения поля balance.
     *
     * @return Возвращает Pair<LocalDateTime, LocalDateTime> с двумя числами. 1 число - это момент вызова метода,
     * 2 число случайно сгенерированное в ренже {@link DateGeneratorImpl#maxDurationInMinute}.
     */
    @Override
    public Pair<LocalDateTime, LocalDateTime> generateStartEndCallTimePair() {
        return Pair.of(LocalDateTime.now(), LocalDateTime.now().plusSeconds(ThreadLocalRandom.current()
                .nextInt(maxDurationInMinute)));

    }
}
