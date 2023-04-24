package ru.sladkkov.cdr.service.datagenerator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.exception.TypeCalsIsEmptyException;
import ru.sladkkov.cdr.service.datagenerator.TypeCallGenerator;
import ru.sladkkov.common.dto.TypeCallDto;
import ru.sladkkov.common.mapper.TypeCallMapper;
import ru.sladkkov.common.model.TypeCall;
import ru.sladkkov.common.repository.TypeCallRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для случайного выбора типа звонка.
 * @author Danila Sladkov
 */
@Component
@RequiredArgsConstructor
public class TypeCallGeneratorImpl implements TypeCallGenerator {

    /**
     * Поле - репозиторий для TypeCall
     */
    private final TypeCallRepository typeCallRepository;

    /**
     * Поле - Маппер для TypeCall из Mapstruct
     */
    private final TypeCallMapper typeCallMapper;

    /**
     * Поле - Список звонков. В рамках нашнего задания TypeCall не расширяемая коллекция состоящая только из 01 и 02.
     * Поэтому решил хранить список, который инициализируется только при Запуске приложения.
     */
    private List<TypeCall> typeCalls;

    /**
     * Метод инициализирует typeCalls в момент запуска приложения
     */
    @PostConstruct
    private void init() {
        typeCalls = typeCallRepository.findAll();
    }


    /**
     * Метод выбора случайного типа звонка.
     *
     * @return возвращает случайно выбранный тип звонка {@link TypeCallDto}.
     * @throws TypeCalsIsEmptyException если таблица тарифов в базе данных пустая.
     */
    @Override
    public TypeCallDto generateTypeCall() {

        if (typeCalls.isEmpty()) {
            throw new TypeCalsIsEmptyException("TypeCals table is empty");
        }
        var typeCall = typeCalls.get(ThreadLocalRandom.current().nextInt(typeCalls.size()));
        return typeCallMapper.toDto(typeCall);

    }

}
