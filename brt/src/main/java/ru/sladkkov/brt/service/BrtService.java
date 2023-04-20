package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BrtService {

    private final AbonentService abonentService;
    private final KafkaTemplate<String, CallDataRecordPlusDto> kafkaTemplate;

    @KafkaListener(groupId = "1", topics = "randomCdr")
    public void authorizeCdr(CallDataRecordDto callDataRecordDto) {

        var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());

        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {

            var data = new CallDataRecordPlusDto(callDataRecordDto, abonentByTelephoneNumber.getTariffDto());

            kafkaTemplate.send("hrs-topic", data);
        }
    }


}
