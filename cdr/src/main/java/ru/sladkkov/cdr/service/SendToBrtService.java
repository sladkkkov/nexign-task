package ru.sladkkov.cdr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.impl.CdrGenerationServiceImpl;
import ru.sladkkov.common.dto.CallDataRecordDto;

@Service
@RequiredArgsConstructor
public class SendToBrtService {

    private final KafkaTemplate<String, CallDataRecordDto> kafkaTemplate;
    private final CdrGenerationServiceImpl cdrGenerationServiceImpl;

    @Scheduled(fixedDelay = 1000000)
    public void sendToBrt() {

        kafkaTemplate.send("randomCdr", cdrGenerationServiceImpl.generateRandomCdr());
    }
}
