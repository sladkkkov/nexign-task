package ru.sladkkov.cdr.service.datagenerator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.exception.TariffIsEmptyException;
import ru.sladkkov.cdr.repository.TypeCallRepository;
import ru.sladkkov.cdr.service.datagenerator.TypeCallGenerator;
import ru.sladkkov.common.dto.TypeCallDto;
import ru.sladkkov.common.mapper.TypeCallMapper;
import ru.sladkkov.common.model.TypeCall;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class TypeCallGeneratorImpl implements TypeCallGenerator {

    private final TypeCallRepository typeCallRepository;
    private final TypeCallMapper typeCallMapper;

    private List<TypeCall> typeCalls;

    @PostConstruct
    private void init() {
        typeCalls = typeCallRepository.findAll();
    }

    @Override
    public TypeCallDto generateTypeCall() {

        if (typeCalls.isEmpty()) {
            throw new TariffIsEmptyException("TypeCals table is empty");
        }
        var typeCall = typeCalls.get(ThreadLocalRandom.current().nextInt(typeCalls.size()));
        return typeCallMapper.toDto(typeCall);

    }

}
