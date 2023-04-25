package ru.sladkkov.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallDto {
    private String typeCall;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String duration;
    private BigDecimal cost;
}
