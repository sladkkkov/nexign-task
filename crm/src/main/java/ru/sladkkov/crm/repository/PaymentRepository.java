package ru.sladkkov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sladkkov.common.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
