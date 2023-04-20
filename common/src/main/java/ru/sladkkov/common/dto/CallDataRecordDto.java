package ru.sladkkov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sladkkov.common.enums.TypeCall;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallDataRecordDto {
    private TypeCall typeCall;
    private String abonentNumber;
    private LocalDateTime dateAndTimeStartCall;
    private LocalDateTime dateAndTimeEndCall;
}
