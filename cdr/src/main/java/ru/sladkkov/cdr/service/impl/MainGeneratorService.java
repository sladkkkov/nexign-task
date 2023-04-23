package ru.sladkkov.cdr.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.AbonentGeneratorService;
import ru.sladkkov.cdr.service.CdrGeneratorService;
import ru.sladkkov.cdr.service.filewriter.CdrFileWriter;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.service.ManagerService;

import java.io.IOException;


@Service
@RequiredArgsConstructor
class MainGeneratorService {

    @Value("${cdr.generate.count.cdr}")
    private int countCdr;
    @Value("${cdr.generate.count.abonent}")
    private int countAbonent;
    private final CdrFileWriter cdrFileWriter;
    private final ManagerService managerService;
    private final AbonentGeneratorService abonentGeneratorService;
    private final CdrGeneratorService cdrGeneratorService;

    private final KafkaTemplate<String, CallDataRecordDto> kafkaTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void generateAbonents() {
        for (int i = 0; i < countAbonent; i++) {
            managerService.createAbonent(abonentGeneratorService.generateAbonent());
        }
    }

    @Scheduled(fixedDelay = 100000)
    public void generateCdrs() throws IOException {
        for (int i = 0; i < countCdr; i++) {
            var callDataRecordDto = cdrGeneratorService.generateCdr();
            kafkaTemplate.send("brt-topic", callDataRecordDto);
            cdrFileWriter.writeFile(callDataRecordDto);
        }
    }
}

