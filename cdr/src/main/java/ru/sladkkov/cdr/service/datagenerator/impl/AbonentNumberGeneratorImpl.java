package ru.sladkkov.cdr.service.datagenerator.impl;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.AbonentNumberGenerator;

@Component
@RequiredArgsConstructor
public class AbonentNumberGeneratorImpl implements AbonentNumberGenerator {
    private final Faker faker;

    @Override
    public String generateAbonentNumber() {
        return faker.phoneNumber().phoneNumber();

    }
}
