package ru.sladkkov.common.dto.abonent;

import lombok.Data;
import ru.sladkkov.common.dto.TariffDto;

import java.math.BigDecimal;

@Data
public class AbonentDto {
    private String numberPhone;
    private TariffDto tariffDto;
    private BigDecimal balance;
}
