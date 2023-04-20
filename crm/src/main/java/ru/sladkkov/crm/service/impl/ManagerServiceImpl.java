package ru.sladkkov.crm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.ChangeTariffDto;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.crm.service.ManagerService;


@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final AbonentRepository abonentRepository;
    private final TariffServiceImpl tariffServiceImpl;

    @Override
    public Abonent createAbonent(Abonent abonent) {
        return abonentRepository.save(abonent);
    }

    @Override
    public ChangeTariffDto changeAbonentTariff(ChangeTariffDto changeTariffDto) {

        var tariff = tariffServiceImpl.findTariffById(changeTariffDto.getTariffId());

        var abonent = abonentRepository.findById(changeTariffDto.getTariffId())
                .orElseThrow(() -> new AbonentNotFoundException("Такого абонента не существует"));

        abonent.setTariff(tariff);

        abonentRepository.save(abonent);

        changeTariffDto.setTariffId(abonent.getId());
        return changeTariffDto;
    }

}
