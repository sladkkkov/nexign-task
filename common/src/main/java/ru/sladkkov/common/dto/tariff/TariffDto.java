package ru.sladkkov.common.dto.tariff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TariffDto {
    private String tariffId;
    private String name;
    private BigDecimal fixedPrice;
    private Integer freeMinuteForFixedPrice;
    private BigDecimal priceForMinute;
    private Integer actionMinute;
    private BigDecimal actionPrice;
    private Boolean incomingFree;
    private Boolean outgoingFree;
    private Boolean freeInsideNetwork;
}
