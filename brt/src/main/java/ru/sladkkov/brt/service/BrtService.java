package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import ru.sladkkov.brt.exception.TypeCallIsNotExistException;
import ru.sladkkov.brt.mapper.CallInfoMapper;
import ru.sladkkov.brt.repository.CallInfoRepository;
import ru.sladkkov.brt.repository.TypeCallRepository;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.dto.abonent.AbonentDto;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.repository.AbonentRepository;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrtService {

    private final AbonentService abonentService;
    private final CallInfoRepository callInfoRepository;
    private final CallInfoMapper callInfoMapper;
    private final TypeCallRepository typeCallRepository;
    private final ReplyingKafkaTemplate<String, CallDataRecordPlusDto, CallInfoDto> template;
    private final AbonentRepository abonentRepository;

    @KafkaListener(groupId = "brt-topic-default", topics = "brt-topic")
    public void kafkaRequestReply(CallDataRecordDto callDataRecordDto) throws ExecutionException, InterruptedException, TimeoutException {

        var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());

        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {

            var data = new CallDataRecordPlusDto(callDataRecordDto, abonentByTelephoneNumber.getTariffDto());

            ProducerRecord<String, CallDataRecordPlusDto> plusDtoProducerRecord = new ProducerRecord<>("hrs-topic", data);
            RequestReplyFuture<String, CallDataRecordPlusDto, CallInfoDto> replyFuture = template.sendAndReceive(plusDtoProducerRecord);

            ConsumerRecord<String, CallInfoDto> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

            saveCallInfo(consumerRecord.value(), abonentByTelephoneNumber);
        }

    }

    private void saveCallInfo(CallInfoDto callInfoDto, AbonentDto abonentDto) {
        var callInfo = callInfoMapper.toEntity(callInfoDto);

        callInfo.setTypeCall(typeCallRepository
                .findByCode(callInfo.getTypeCall().getCode())
                .orElseThrow(() -> new TypeCallIsNotExistException("Такой тип не существует")));


        callInfo.setAbonent(abonentRepository.findByAbonentNumber(abonentDto.getAbonentNumber())
                .orElseThrow(() -> new AbonentNotFoundException("Абонент не найден")));

        callInfoRepository.save(callInfo);
    }

}
