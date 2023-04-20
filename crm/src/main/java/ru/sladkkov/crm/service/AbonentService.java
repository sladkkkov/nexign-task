package ru.sladkkov.crm.service;

import org.springframework.stereotype.Service;
import ru.sladkkov.common.model.Payment;

@Service
public interface AbonentService {
    Payment pay(Payment payment);
}
