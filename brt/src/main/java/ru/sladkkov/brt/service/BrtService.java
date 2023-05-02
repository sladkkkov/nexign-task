package ru.sladkkov.brt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sladkkov.brt.exception.TypeCallIsNotExistException;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.mapper.CallInfoMapper;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.common.repository.CallInfoRepository;
import ru.sladkkov.common.repository.TypeCallRepository;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.dto.tariff.mapper.TariffMapper;


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

    @Value("${nexign.send-hrs-topic}")
    private String hrsTopic;
    private final AbonentService abonentService;
    private final CallInfoRepository callInfoRepository;
    private final CallInfoMapper callInfoMapper;
    private final TariffMapper tariffMapper;
    private final TypeCallRepository typeCallRepository;
    private final ReplyingKafkaTemplate<String, CallDataRecordPlusDto, CallInfoDto> replyingKafkaTemplate;
    private final AbonentRepository abonentRepository;

    @KafkaListener(groupId = "brt-topic-default", topics = "brt-topic")
    @Transactional
    public void kafkaRequestReply(CallDataRecordDto callDataRecordDto) throws ExecutionException, InterruptedException, TimeoutException {

        var abonentByTelephoneNumber = abonentService.findAbonentByTelephoneNumber(callDataRecordDto.getAbonentNumber());
        if (abonentByTelephoneNumber.getBalance().compareTo(BigDecimal.ZERO) >= 0) {

            var data = new CallDataRecordPlusDto(callDataRecordDto, tariffMapper.toDto(abonentByTelephoneNumber.getTariff()));

            ProducerRecord<String, CallDataRecordPlusDto> plusDtoProducerRecord = new ProducerRecord<>(hrsTopic, data);
            RequestReplyFuture<String, CallDataRecordPlusDto, CallInfoDto> replyFuture = replyingKafkaTemplate.sendAndReceive(plusDtoProducerRecord);
            ConsumerRecord<String, CallInfoDto> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

            if (consumerRecord == null) {
                throw new TimeoutException("Time is out, callInfoDto dont receive");
            }

            var callInfoDto = consumerRecord.value();
            saveCallInfo(callInfoDto, abonentByTelephoneNumber);

            updateBalance(abonentByTelephoneNumber, callInfoDto);
            updateCountMinute(abonentByTelephoneNumber, callInfoDto);
        }
    }

    public void updateBalance(Abonent abonent, CallInfoDto callInfoDto) {
        abonent.setBalance(abonent.getBalance().subtract(callInfoDto.getCost()));

        abonentRepository.save(abonent);
    }

    public void updateCountMinute(Abonent abonent, CallInfoDto callInfoDto) {
        var countMinute = abonent.getCountMinuteByTariffPeriod() +
                Duration.between(LocalTime.MIN, LocalTime.parse(callInfoDto.getDuration())).toMinutes();

        abonent.setCountMinuteByTariffPeriod((int) countMinute);
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
