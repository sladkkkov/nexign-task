package ru.sladkkov.crm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sladkkov.common.dto.PaymentDto;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.mapper.PaymentMapper;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.crm.repository.PaymentRepository;
import ru.sladkkov.crm.service.AbonentService;


@Service
@RequiredArgsConstructor
public class AbonentServiceImpl implements AbonentService {

    private final PaymentMapper paymentMapper;
    private final AbonentRepository abonentRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentDto pay(PaymentDto paymentDto) {

        var abonent = abonentRepository.findByAbonentNumber(paymentDto.getAbonentPhone()).orElseThrow(() ->
                new AbonentNotFoundException("Абонент с таким номером телефона не найден"));

        abonent.setBalance(abonent.getBalance().add(paymentDto.getMoney()));
        abonentRepository.save(abonent);

        var payment = paymentMapper.toEntity(paymentDto);
        payment.setAbonent(abonent);
        var save = paymentRepository.save(payment);

        paymentDto.setId(save.getId());

        return paymentDto;

    }
}
