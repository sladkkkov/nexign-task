package ru.sladkkov.common.dto;

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
    private Long tariffId;
    private String name;
    private BigDecimal fixedPrice;
    private Integer freeMinuteForFixedPrice;
    private BigDecimal priceForMinute;
    private Boolean incomingPaid;
    private Boolean outgoingPaid;
    private Boolean freeInsideNetwork;
}
