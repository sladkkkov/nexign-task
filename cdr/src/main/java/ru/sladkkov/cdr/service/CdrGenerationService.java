package ru.sladkkov.cdr.service;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.dto.CallDataRecordDto;
import ru.sladkkov.cdr.enums.TypeCall;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CdrGenerationService {

    private final Faker faker;

    //TODO Сейчас dateAndTimeStartCall - timeNow будешь расхождение с End Call на время вызова методов, пофиксить.
    public CallDataRecordDto generateRandomCdr() {
        return CallDataRecordDto.builder()
                .typeCall(randomTypeCall())
                .clientNumber(randomClientNumber())
                .dateAndTimeStartCall(LocalDateTime.now())
                .dateAndTimeEndCall(randomSecondLocalDate())
                .build();
    }

    private TypeCall randomTypeCall() {
        return TypeCall.TYPE_CALLS.get(ThreadLocalRandom
                .current()
                .nextInt(TypeCall.TYPE_CALLS.size()));
    }

    private String randomClientNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    private LocalDateTime randomSecondLocalDate() {
        return LocalDateTime.now().plusSeconds(ThreadLocalRandom.current().nextInt(3600));
    }
}
