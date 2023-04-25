package ru.sladkkov.hrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.hrs.tariff.factory.TariffFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final AbonentRepository abonentRepository;

    @KafkaListener(groupId = "hrs-topic-default", topics = "hrs-topic", containerFactory = "kafkaListenerContainerFactory")
    @SendTo("brt-reply-topic")
    public CallInfoDto calculatePriceCall(CallDataRecordPlusDto callDataRecordPlusDto) {
        var callDataRecordDto = callDataRecordPlusDto.getCallDataRecordDto();

        var tariffId = callDataRecordPlusDto.getTariff();
        var tariffByTariffId = TariffFactory.createCdr(tariffId.getTariffId());


        var totalDuration = calculateDurationCall(callDataRecordDto.getDateAndTimeStartCall(),
                callDataRecordDto.getDateAndTimeEndCall());


        var cost = tariffByTariffId.calculateCallPrice(callDataRecordDto, getTotalDuration(callDataRecordDto));

        return sendResult(callDataRecordDto, totalDuration, cost);

    }

    private long getTotalDuration(CallDataRecordDto callDataRecordDto) {
        var abonentByTelephoneNumber = abonentRepository
                .findByAbonentNumber(callDataRecordDto.getAbonentNumber())
                .orElseThrow(() -> new AbonentNotFoundException("Такого абонента не существует"));

        return abonentByTelephoneNumber.getCountMinuteByTariffPeriod();
    }

    private CallInfoDto sendResult(CallDataRecordDto callDataRecordDto, Duration totalDuration, BigDecimal cost) {

        String timeInHHMMSS = getDurationToStringFormat(totalDuration);

        return CallInfoDto.builder()
                .typeCall(callDataRecordDto.getTypeCall())
                .cost(cost)
                .startTime(callDataRecordDto.getDateAndTimeStartCall())
                .endTime(callDataRecordDto.getDateAndTimeEndCall())
                .duration(timeInHHMMSS)
                .build();
    }

    private String getDurationToStringFormat(Duration totalDuration) {
        long hh = totalDuration.toHours();
        long mm = totalDuration.toMinutesPart();
        long ss = totalDuration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    private Duration calculateDurationCall(LocalDateTime dateAndTimeStartCall, LocalDateTime dateAndTimeEndCall) {

        return Duration.between(dateAndTimeStartCall, dateAndTimeEndCall)
                .truncatedTo(ChronoUnit.MINUTES)
                .plusMinutes(1);
    }

}
