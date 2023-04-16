package ru.sladkkov.cdr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sladkkov.cdr.enums.TypeCall;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallDataRecordDto {
    private TypeCall typeCall;
    private String clientNumber;
    private LocalDateTime dateAndTimeStartCall;
    private LocalDateTime dateAndTimeEndCall;
}
