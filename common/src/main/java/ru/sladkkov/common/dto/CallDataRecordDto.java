package ru.sladkkov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallDataRecordDto {
    private TypeCallDto typeCall;
    private String abonentNumber;
    private LocalDateTime dateAndTimeStartCall;
    private LocalDateTime dateAndTimeEndCall;
}
