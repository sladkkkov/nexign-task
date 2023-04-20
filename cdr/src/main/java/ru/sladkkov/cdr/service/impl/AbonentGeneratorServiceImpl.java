package ru.sladkkov.cdr.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.AbonentGeneratorService;
import ru.sladkkov.cdr.service.datagenerator.AbonentNumberGenerator;
import ru.sladkkov.cdr.service.datagenerator.BalanceGenerator;
import ru.sladkkov.cdr.service.datagenerator.TariffGenerator;
import ru.sladkkov.common.model.Abonent;

@Service
@RequiredArgsConstructor
public class AbonentGeneratorServiceImpl implements AbonentGeneratorService {

    private final AbonentNumberGenerator abonentNumberGenerator;
    private final BalanceGenerator balanceGenerator;
    private final TariffGenerator tariffGenerator;

    @Override
    public Abonent generateAbonent() {
        return Abonent.builder()
                .telephoneNumber(abonentNumberGenerator.generateAbonentNumber())
                .tariff(tariffGenerator.generateTariff())
                .balance(balanceGenerator.generateBalance())
                .build();
    }
}
