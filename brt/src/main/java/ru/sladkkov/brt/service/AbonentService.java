package ru.sladkkov.brt.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.abonent.AbonentDto;
import ru.sladkkov.common.dto.abonent.mapper.AbonentMapper;
import ru.sladkkov.common.dto.tariff.mapper.TariffMapper;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.repository.AbonentRepository;

@Service
@RequiredArgsConstructor
public class AbonentService {

    private final AbonentMapper abonentMapper;
    private final TariffMapper tariffMapper;
    private final AbonentRepository abonentRepository;

    public AbonentDto findAbonentByTelephoneNumber(String telephoneNumber) {

        Abonent abonent = abonentRepository.findAbonentByAbonentNumber(telephoneNumber)
                .orElseThrow(() -> new AbonentNotFoundException("Пользователя с таким телефоном не найдено"));

        var abonentDto = abonentMapper.toDto(abonent);
        abonentDto.setTariffDto(tariffMapper.toDto(abonent.getTariff()));
        return abonentDto;
    }

}
