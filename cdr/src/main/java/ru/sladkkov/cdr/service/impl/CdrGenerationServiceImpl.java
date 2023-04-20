package ru.sladkkov.cdr.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.CdrGeneratorService;
import ru.sladkkov.cdr.service.datagenerator.AbonentNumberGenerator;
import ru.sladkkov.cdr.service.datagenerator.DateGenerator;
import ru.sladkkov.cdr.service.datagenerator.TypeCallGenerator;
import ru.sladkkov.common.dto.CallDataRecordDto;

@Service
@RequiredArgsConstructor
public class CdrGenerationServiceImpl implements CdrGeneratorService {
    private final TypeCallGenerator typeCallGenerator;
    private final AbonentNumberGenerator abonentNumberGenerator;
    private final DateGenerator dateGenerator;

    @Override
    public CallDataRecordDto generateCdr() {
        var localDateTimeLocalDateTimePair = dateGenerator.generateStartEndCallTimePair();
        return CallDataRecordDto.builder()
                .typeCall(typeCallGenerator.generateTypeCall())
                .abonentNumber(abonentNumberGenerator.generateAbonentNumber())
                .dateAndTimeStartCall(localDateTimeLocalDateTimePair.getFirst())
                .dateAndTimeEndCall(localDateTimeLocalDateTimePair.getSecond())
                .build();
    }
}
