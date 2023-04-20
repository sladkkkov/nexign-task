package ru.sladkkov.cdr.service.datagenerator;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public interface DateGenerator {
    Pair<LocalDateTime, LocalDateTime> generateStartEndCallTimePair();

}
