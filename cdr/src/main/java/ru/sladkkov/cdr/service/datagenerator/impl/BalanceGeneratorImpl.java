package ru.sladkkov.cdr.service.datagenerator.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.BalanceGenerator;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BalanceGeneratorImpl implements BalanceGenerator {

    @Value("${cdr.generate.balance.range}")
    private int balanceRange;

    @Override
    public BigDecimal generateBalance() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(balanceRange));
    }
}
