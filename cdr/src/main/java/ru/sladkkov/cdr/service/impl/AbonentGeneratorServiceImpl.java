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

    private static final Integer COUNT_MINUTE_BY_START_TARIFF_PERIOD = 0;
    private final AbonentNumberGenerator abonentNumberGenerator;
    private final BalanceGenerator balanceGenerator;
    private final TariffGenerator tariffGenerator;
    private int i = 0;

    /**
     * Метод сборки экземпляра Abonent.
     *
     * @return возвращает случайно сгенерированного Абонента {@link Abonent}.
     */
    @Override
    public Abonent generateAbonent() {
        return Abonent.builder()
                .abonentNumber(abonentNumberGenerator.getNumbers().get(i++))
                .tariff(tariffGenerator.getRandomTariff())
                .balance(balanceGenerator.generateBalance())
                .countMinuteByTariffPeriod(COUNT_MINUTE_BY_START_TARIFF_PERIOD)
                .build();
    }
}
