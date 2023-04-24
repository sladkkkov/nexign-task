package ru.sladkkov.cdr.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sladkkov.cdr.service.AbonentGeneratorService;
import ru.sladkkov.cdr.service.CdrGeneratorService;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.service.ManagerService;

@Service
@RequiredArgsConstructor
class MainGeneratorService {

    @Value("${cdr.generate.count.cdr}")
    private int countCdr;
    @Value("${cdr.generate.count.abonent}")
    private int countAbonent;

    private final ManagerService managerService;
    private final AbonentGeneratorService abonentGeneratorService;
    private final CdrGeneratorService cdrGeneratorService;
    private final KafkaTemplate<String, CallDataRecordDto> kafkaTemplate;

    /**
     * Метод генерации Abonent, запускается при старте приложения.
     * Сохраняет сгенерированного Абонента в БД. {@link ru.sladkkov.common.model.Abonent}.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void generateAbonents() {
        for (int i = 0; i < countAbonent; i++) {
            managerService.createAbonent(abonentGeneratorService.generateAbonent());
        }
    }

    /**
     * Метод генерации Cdr, запускается при старте приложения.
     * Отправляет в топик Кафки brt-topic, сгенерированную cdr. {@link CallDataRecordDto}.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void generateCdrs() {
        for (int i = 0; i < countCdr; i++) {
            var callDataRecordDto = cdrGeneratorService.generateCdr();
            kafkaTemplate.send("brt-topic", callDataRecordDto);
        }
    }
}
