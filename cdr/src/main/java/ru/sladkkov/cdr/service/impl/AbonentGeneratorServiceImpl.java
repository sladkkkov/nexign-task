package ru.sladkkov.cdr.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.AbonentGeneratorService;
import ru.sladkkov.cdr.service.datagenerator.AbonentNumberGenerator;
import ru.sladkkov.cdr.service.datagenerator.BalanceGenerator;
import ru.sladkkov.cdr.service.datagenerator.TariffGenerator;
import ru.sladkkov.common.model.Abonent;


@Service
@RequiredArgsConstructor
public class AbonentGeneratorServiceImpl implements AbonentGeneratorService {

    @Value("${randommer.api.count}")
    private int countNumber;
    private final AbonentNumberGenerator abonentNumberGenerator;
    private final BalanceGenerator balanceGenerator;
    private final TariffGenerator tariffGenerator;
    private int i = 0;

    @Override
    public Abonent generateAbonent() {
        return Abonent.builder()
                .abonentNumber(abonentNumberGenerator.getNumbers().get(i++))
                .tariff(tariffGenerator.generateTariff())
                .balance(balanceGenerator.generateBalance())
                .countMinuteByTariffPeriod(0)
                .build();
    }
}
