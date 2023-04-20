package ru.sladkkov.cdr.service.datagenerator.impl;

import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.TypeCallGenerator;
import ru.sladkkov.common.enums.TypeCall;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class TypeCallGeneratorImpl implements TypeCallGenerator {
    @Override
    public TypeCall generateTypeCall() {
        return TypeCall.TYPE_CALLS.get(ThreadLocalRandom
                .current()
                .nextInt(TypeCall.TYPE_CALLS.size()));
    }
}
