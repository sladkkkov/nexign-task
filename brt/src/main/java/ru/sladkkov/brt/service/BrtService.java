package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sladkkov.brt.model.Abonent;
import ru.sladkkov.brt.repository.AbonentRepository;
import ru.sladkkov.dtoshare.dto.CallDataRecordDto;
import ru.sladkkov.dtoshare.dto.CallDataRecordPlusDto;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BrtService {

    private final AbonentRepository abonentRepository;

    private final KafkaTemplate<String, CallDataRecordPlusDto> kafkaTemplate;

    @KafkaListener(groupId = "1", topics = "randomCdr")
    public void authorizeCdr(CallDataRecordDto callDataRecordDto) {

        Abonent abonentByTelephoneNumber = abonentRepository
                .findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());

        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
            kafkaTemplate.send("hrs-topic",
                    new CallDataRecordPlusDto(callDataRecordDto, abonentByTelephoneNumber.getTariffId()));
        }
    }


}
