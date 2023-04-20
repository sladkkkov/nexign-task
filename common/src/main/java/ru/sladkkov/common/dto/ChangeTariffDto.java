package ru.sladkkov.common.dto;

import lombok.Data;

@Data
public class ChangeTariffDto {

    private Long id;
    private String numberPhone;
    private Long tariffId;
}
