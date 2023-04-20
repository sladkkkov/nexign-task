package ru.sladkkov.cdr.service.impl;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.CdrGeneratorService;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.enums.TypeCall;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CdrGenerationServiceImpl implements CdrGeneratorService {

    private final Faker faker;

    //TODO Сейчас dateAndTimeStartCall - timeNow будешь расхождение с End Call на время вызова методов, пофиксить.
    public CallDataRecordDto generateRandomCdr() {
        return CallDataRecordDto.builder()
                .typeCall(randomTypeCall())
                .abonentNumber(randomClientNumber())
                .dateAndTimeStartCall(LocalDateTime.now())
                .dateAndTimeEndCall(randomSecondLocalDate())
                .build();
    }

    @Override
    public TypeCall randomTypeCall() {
        return TypeCall.TYPE_CALLS.get(ThreadLocalRandom
                .current()
                .nextInt(TypeCall.TYPE_CALLS.size()));
    }

    @Override
    public String randomClientNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    @Override
    public LocalDateTime randomSecondLocalDate() {
        return LocalDateTime.now().plusSeconds(ThreadLocalRandom.current().nextInt(3600));
    }
}
