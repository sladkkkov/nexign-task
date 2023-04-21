package ru.sladkkov.common.dto.abonent;

import lombok.Data;
import ru.sladkkov.common.dto.tariff.TariffDto;

import java.math.BigDecimal;

@Data
public class AbonentDto {
    private String abonentNumber;
    private TariffDto tariffDto;
    private BigDecimal balance;
}
