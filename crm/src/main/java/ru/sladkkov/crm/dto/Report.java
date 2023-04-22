package ru.sladkkov.crm.dto;

import ru.sladkkov.common.dto.PaymentDto;

import java.math.BigDecimal;
import java.util.List;

public class Report {
    private Long id;
    private String abonentNumber;
    private String tariffIndex;

    private List<PaymentDto> payloadList;

    private BigDecimal tootalCost;
    private String monetartUnit;
}
