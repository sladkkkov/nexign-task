package ru.sladkkov.brt.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.abonent.mapper.AbonentMapper;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.repository.AbonentRepository;

@Service
@RequiredArgsConstructor
public class AbonentService {

    private final AbonentMapper abonentMapper;
    private final AbonentRepository abonentRepository;

    public ru.sladkkov.common.dto.abonent.AbonentDto findAbonentByTelephoneNumber(String telephoneNumber) {

        Abonent abonent = abonentRepository.findAbonentByTelephoneNumber(telephoneNumber)
                .orElseThrow(() -> new AbonentNotFoundException("Пользователя с таким телефоном не найдено"));

        return abonentMapper.toDto(abonent);
    }

}
