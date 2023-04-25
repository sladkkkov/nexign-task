package ru.sladkkov.hrs.tariff.factory;


import ru.sladkkov.common.dto.CallDataRecordDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public interface Tariffable {

    BigDecimal calculateCallPrice(CallDataRecordDto callDataRecordDto, Long sumTotalDuration);

    default Duration calculateDurationForOneCall(LocalDateTime dateAndTimeStartCall,
                                                 LocalDateTime dateAndTimeEndCall) {

        return Duration.between(dateAndTimeStartCall,
                dateAndTimeEndCall).truncatedTo(ChronoUnit.MINUTES).plusMinutes(1);
    }

}
