package ru.sladkkov.cdr.service;

import ru.sladkkov.common.dto.CallDataRecordDto;

public interface CdrGeneratorService {
    CallDataRecordDto generateCdr();
}
