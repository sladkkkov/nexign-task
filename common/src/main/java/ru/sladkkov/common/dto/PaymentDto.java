package ru.sladkkov.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private Long id;
    private String abonentPhone;
    private BigDecimal money;

}
