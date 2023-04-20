package ru.sladkkov.crm.dto.payment.mapper;

import org.mapstruct.Mapper;
import ru.sladkkov.common.model.Payment;
import ru.sladkkov.crm.dto.payment.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);

    Payment toModel(PaymentDto paymentDto);
}

