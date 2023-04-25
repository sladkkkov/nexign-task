package ru.sladkkov.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {
    private Long id;
    private String abonentNumber;
    private String tariffIndex;
    private List<CallDto> payments;
    private BigDecimal totalCost;
    private String monetaryUnit;
}
