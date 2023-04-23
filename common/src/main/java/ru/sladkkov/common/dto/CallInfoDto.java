package ru.sladkkov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sladkkov.common.dto.abonent.AbonentDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallInfoDto {
    private TypeCallDto typeCall;
    private AbonentDto abonent;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String duration;
    private BigDecimal cost;
}