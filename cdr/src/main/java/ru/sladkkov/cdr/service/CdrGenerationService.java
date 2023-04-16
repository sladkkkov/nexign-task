package ru.sladkkov.cdr.service;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.dto.CallDataRecordDto;
import ru.sladkkov.cdr.enums.TypeCall;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CdrGenerationService {

    private final Faker faker;
    private final KafkaTemplate<String, CallDataRecordDto> kafkaTemplate;

    //TODO Сейчас dateAndTimeStartCall - timeNow будешь расхождение с End Call на время вызова методов, пофиксить.
    @Scheduled(fixedDelay = 10000)
    public void generateRandomCdr() {

        LocalDateTime timeNow = LocalDateTime.now();
        CallDataRecordDto callDataRecordDto = CallDataRecordDto.builder()
                .typeCall(randomTypeCall())
                .clientNumber(randomClientNumber())
                .dateAndTimeStartCall(timeNow)
                .dateAndTimeEndCall(randomSecondLocalDate())
                .build();

        kafkaTemplate.send("randomCdr", callDataRecordDto);
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
