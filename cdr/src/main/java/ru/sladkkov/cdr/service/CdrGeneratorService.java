package ru.sladkkov.cdr.service;

import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.enums.TypeCall;

import java.time.LocalDateTime;

public interface CdrGeneratorService {
    CallDataRecordDto generateRandomCdr();

    TypeCall randomTypeCall();

    String randomClientNumber();

    LocalDateTime randomSecondLocalDate();
}
