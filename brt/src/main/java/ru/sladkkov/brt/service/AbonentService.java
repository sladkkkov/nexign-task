package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.repository.AbonentRepository;

@Service
@RequiredArgsConstructor
public class AbonentService {

    private final AbonentRepository abonentRepository;

    public Abonent findAbonentByTelephoneNumber(String telephoneNumber) {

        return abonentRepository.findByAbonentNumber(telephoneNumber)
                .orElseThrow(() -> new AbonentNotFoundException("Пользователя с таким телефоном не найдено"));
    }

}
