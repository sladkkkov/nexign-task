package ru.sladkkov.cdr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.dto.CallDataRecordDto;

@Service
@RequiredArgsConstructor
public class SendToBrtService {

    private final KafkaTemplate<String, CallDataRecordDto> kafkaTemplate;
    private final CdrGenerationService cdrGenerationService;

    @Scheduled(fixedDelay = 1000000)
    public void sendToBrt() {

        kafkaTemplate.send("randomCdr", cdrGenerationService.generateRandomCdr());
    }
}
