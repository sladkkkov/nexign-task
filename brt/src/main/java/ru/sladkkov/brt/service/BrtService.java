package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.sladkkov.brt.exception.TypeCallIsNotExistException;
import ru.sladkkov.brt.mapper.CallInfoMapper;
import ru.sladkkov.brt.repository.TypeCallRepository;
import ru.sladkkov.brt.service.impl.CdrPlusFileWriter;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.repository.CallInfoRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrtService {

    private final AbonentService abonentService;
    private final DataParserService dataParserService;
    private final CdrPlusFileWriter cdrPlusFileWriter;
    private final CallInfoRepository callInfoRepository;
    private final CallInfoMapper callInfoMapper;
    private final TypeCallRepository typeCallRepository;
    private final KafkaTemplate<String, CallDataRecordPlusDto> kafkaTemplate;

    @KafkaListener(groupId = "brt-topic-default", topics = "brt-topic")
    public void authorizeCdr(CallDataRecordDto callDataRecordDto) {

        var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());

        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {

            var data = new CallDataRecordPlusDto(callDataRecordDto, abonentByTelephoneNumber.getTariffDto());

            kafkaTemplate.send("hrs-topic", data);
        }
    }

    @KafkaListener(groupId = "brt-db-topic-group-id", topics = "brt-db-topic")
    public void billingClient(CallInfoDto callInfoDto) {
        var callInfo = callInfoMapper.toEntity(callInfoDto);

        callInfo.setTypeCall(typeCallRepository
                .findByCode(callInfo.getTypeCall().getCode())
                .orElseThrow(() -> new TypeCallIsNotExistException("Такой тип не существует")));


        callInfoRepository.save(callInfo);
    }

    public void run() throws IOException {
        authorizeCdrs(dataParserService.getCallDataRecordDtoFromFile());
    }

    public void authorizeCdrs(List<CallDataRecordDto> callDataRecordDtoList) throws IOException {
        for (CallDataRecordDto dataRecordDto : callDataRecordDtoList) {

            var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(dataRecordDto.getAbonentNumber());

            if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
                cdrPlusFileWriter.writeFile(new CallDataRecordPlusDto(dataRecordDto, abonentByTelephoneNumber.getTariffDto()));
            }
        }
    }


}
