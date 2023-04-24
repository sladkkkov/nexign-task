package ru.sladkkov.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeTariffDto {
    private Long id;
    private String abonentPhone;
    private String tariffId;
}
