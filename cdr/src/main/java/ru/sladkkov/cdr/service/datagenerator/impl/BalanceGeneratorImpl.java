package ru.sladkkov.cdr.service.datagenerator.impl;

import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.BalanceGenerator;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BalanceGeneratorImpl implements BalanceGenerator {
    @Override
    public BigDecimal generateBalance() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt());
    }
}
