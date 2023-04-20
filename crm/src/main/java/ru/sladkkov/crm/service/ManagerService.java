package ru.sladkkov.crm.service;

import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.ChangeTariffDto;
import ru.sladkkov.common.model.Abonent;

@Service
public interface ManagerService {

    Abonent createAbonent(Abonent abonent);

    ChangeTariffDto changeAbonentTariff(ChangeTariffDto changeTariffDto);
}
