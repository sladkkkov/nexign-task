package ru.sladkkov.cdr.service.datagenerator.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.BalanceGenerator;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс генерации случайного баланса.
 *
 * @author Danila Sladkov
 */
@Component
public class BalanceGeneratorImpl implements BalanceGenerator {

    /**
     * Поле - range для генерации баланса
     */
    @Value("${cdr.generate.balance.range}")
    private int maxBalanceRange;

    /**
     * Метод получения значения поля balance.
     *
     * @return возвращает случайно сгенерированный баланс типа BigDecimal в ренже {@link BalanceGeneratorImpl#maxBalanceRange}.
     */
    @Override
    public BigDecimal generateBalance() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(maxBalanceRange));
    }
}
