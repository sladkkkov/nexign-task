package ru.sladkkov.crm.service;

import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.PaymentDto;

@Service
public interface AbonentService {
    PaymentDto pay(PaymentDto payment);
}
