package ru.sladkkov.hrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BillingService {

    @KafkaListener(groupId = "1", topics = "hrs-topic")
    public void calculationPay(CallDataRecordPlusDto callDataRecordPlusDto) {


    }

    private Duration calculateDurationCall(LocalDateTime dateAndTimeStartCall, LocalDateTime dateAndTimeEndCall) {

        return Duration.between(dateAndTimeStartCall, dateAndTimeEndCall)
                .truncatedTo(ChronoUnit.MINUTES)
                .plusMinutes(1);
    }
}
