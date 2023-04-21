package ru.sladkkov.crm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.model.Payment;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.crm.repository.PaymentRepository;
import ru.sladkkov.crm.service.AbonentService;


@Service
@RequiredArgsConstructor
public class AbonentServiceImpl implements AbonentService {

    private final AbonentRepository abonentRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment pay(Payment payment) {

        var abonent = abonentRepository.findAbonentByAbonentNumber(payment.getNumberPhone()).orElseThrow(() ->
                new AbonentNotFoundException("Абонент с таким номером телефона не найден"));

        abonent.setBalance(abonent.getBalance().add(payment.getMoney()));

        paymentRepository.save(payment);
        abonentRepository.save(abonent);

        return payment;
    }
}
