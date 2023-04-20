package ru.sladkkov.cdr.service.datagenerator.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.datagenerator.DateGenerator;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DateGeneratorImpl implements DateGenerator {

    @Value("${call.duration.max}")
    private Integer maxDurationInMinute;

    @Override
    public Pair<LocalDateTime, LocalDateTime> generateStartEndCallTimePair() {
        return Pair.of(LocalDateTime.now(), LocalDateTime.now().plusSeconds(ThreadLocalRandom.current()
                .nextInt(maxDurationInMinute)));

    }
}
