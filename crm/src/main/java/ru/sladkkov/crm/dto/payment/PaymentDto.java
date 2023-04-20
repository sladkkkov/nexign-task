package ru.sladkkov.crm.dto.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private Long id;
    private String numberPhone;
    private BigDecimal money;

    private String monetaryUnit;
}
