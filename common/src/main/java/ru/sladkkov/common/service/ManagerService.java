package ru.sladkkov.common.service;

import ru.sladkkov.common.dto.ChangeTariffDto;
import ru.sladkkov.common.model.Abonent;

public interface ManagerService {

    Abonent createAbonent(Abonent abonent);

    ChangeTariffDto changeAbonentTariff(ChangeTariffDto changeTariffDto);
}
