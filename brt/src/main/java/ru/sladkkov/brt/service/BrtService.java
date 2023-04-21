package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sladkkov.brt.service.impl.CdrPlusFileWriter;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrtService {

    private final AbonentService abonentService;
    private final DataParserService dataParserService;
    private final CdrPlusFileWriter cdrPlusFileWriter;
    private final KafkaTemplate<String, CallDataRecordPlusDto> kafkaTemplate;

    /*
        @KafkaListener(groupId = "1", topics = "randomCdr")
    */
    public void authorizeCdr(CallDataRecordDto callDataRecordDto) {

        var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());

        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {

            var data = new CallDataRecordPlusDto(callDataRecordDto, abonentByTelephoneNumber.getTariffDto());

            kafkaTemplate.send("hrs-topic", data);
        }
    }

    public void run() throws IOException {
        authorizeCdrs(dataParserService.getCallDataRecordDtoFromFile());
    }

    public void authorizeCdrs(List<CallDataRecordDto> callDataRecordDtoList) throws IOException {
        for (CallDataRecordDto dataRecordDto : callDataRecordDtoList) {

            var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(dataRecordDto.getAbonentNumber());

            if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
                log.error(dataRecordDto.toString());
                cdrPlusFileWriter.writeFile(new CallDataRecordPlusDto(dataRecordDto, abonentByTelephoneNumber.getTariffDto()));

            }
        }
    }


}
