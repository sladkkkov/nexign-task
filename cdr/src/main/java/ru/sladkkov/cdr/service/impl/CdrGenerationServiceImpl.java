package ru.sladkkov.cdr.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.CdrGeneratorService;
import ru.sladkkov.cdr.service.datagenerator.AbonentNumberGenerator;
import ru.sladkkov.cdr.service.datagenerator.DateGenerator;
import ru.sladkkov.cdr.service.datagenerator.TypeCallGenerator;
import ru.sladkkov.common.dto.CallDataRecordDto;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CdrGenerationServiceImpl implements CdrGeneratorService {

    @Value("${randommer.api.count}")
    private int countNumber;
    private final TypeCallGenerator typeCallGenerator;
    private final AbonentNumberGenerator abonentNumberGenerator;
    private final DateGenerator dateGenerator;

    @Override
    public CallDataRecordDto generateCdr() {
        var localDateTimeLocalDateTimePair = dateGenerator.generateStartEndCallTimePair();
        return CallDataRecordDto.builder()
                .typeCall(typeCallGenerator.generateTypeCall())
                .abonentNumber(abonentNumberGenerator.getNumbers().get(ThreadLocalRandom.current().nextInt(countNumber)))
                .dateAndTimeStartCall(localDateTimeLocalDateTimePair.getFirst())
                .dateAndTimeEndCall(localDateTimeLocalDateTimePair.getSecond())
                .build();
    }
}
