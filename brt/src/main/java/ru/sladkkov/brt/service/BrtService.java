package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sladkkov.brt.exception.TypeCallIsNotExistException;
import ru.sladkkov.brt.repository.TypeCallRepository;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.dto.tariff.mapper.TariffMapper;
import ru.sladkkov.common.mapper.CallInfoMapper;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.common.repository.CallInfoRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
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
    private final TariffMapper tariffMapper;
    private final TypeCallRepository typeCallRepository;
    private final ReplyingKafkaTemplate<String, CallDataRecordPlusDto, CallInfoDto> replyingKafkaTemplate;
    private final AbonentRepository abonentRepository;

    @KafkaListener(groupId = "brt-topic-default", topics = "brt-topic")
    public void kafkaRequestReply(CallDataRecordDto callDataRecordDto) throws ExecutionException, InterruptedException, TimeoutException {

        var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());

        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {

            var data = new CallDataRecordPlusDto(callDataRecordDto, tariffMapper.toDto(abonentByTelephoneNumber.getTariff()));

            ProducerRecord<String, CallDataRecordPlusDto> plusDtoProducerRecord = new ProducerRecord<>("hrs-topic", data);
            RequestReplyFuture<String, CallDataRecordPlusDto, CallInfoDto> replyFuture = replyingKafkaTemplate.sendAndReceive(plusDtoProducerRecord);
            ConsumerRecord<String, CallInfoDto> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

            var callInfoDto = consumerRecord.value();
            saveCallInfo(callInfoDto, abonentByTelephoneNumber);

            updateBalance(abonentByTelephoneNumber, callInfoDto);
        }

    }

    @Transactional
    public void updateBalance(Abonent abonent, CallInfoDto callInfoDto) {
        abonent.setBalance(abonent.getBalance().subtract(callInfoDto.getCost()));

        var l = abonent.getCountMinuteByTariffPeriod() +
                Duration.between(LocalTime.MIN, LocalTime.parse(callInfoDto.getDuration())).toMinutes();

        abonent.setCountMinuteByTariffPeriod((int) l);
        abonentRepository.save(abonent);
    }

    private void saveCallInfo(CallInfoDto callInfoDto, Abonent abonent) {
        var callInfo = callInfoMapper.toEntity(callInfoDto);

        callInfo.setTypeCall(typeCallRepository
                .findByCode(callInfo.getTypeCall().getCode())
                .orElseThrow(() -> new TypeCallIsNotExistException("Такой тип не существует")));

        callInfo.setAbonent(abonent);

        callInfoRepository.save(callInfo);
    }

}
