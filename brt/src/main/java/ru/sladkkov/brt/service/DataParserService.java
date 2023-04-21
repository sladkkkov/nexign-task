package ru.sladkkov.brt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sladkkov.brt.exception.ParserException;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.enums.TypeCall;
import ru.sladkkov.common.exception.FileCorruptedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DataParserService {
    private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMddHHmmss")
            .toFormatter();
    @Value("${cdr.generate.path}")
    private String url;

    public List<CallDataRecordDto> getCallDataRecordDtoFromFile() {

        List<CallDataRecordDto> callDataRecordDtoList = new ArrayList<>();
        var path = Paths.get(url);

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(t -> {
                var cdr = t.split(", ");

                if (cdr.length != 4) {
                    throw new ParserException("Parser exception for cdrs");
                }

                callDataRecordDtoList.add(new CallDataRecordDto(
                        TypeCall.fromNumericNameOfType(cdr[0]),
                        cdr[1],
                        LocalDateTime.parse(cdr[2], dateTimeFormatter),
                        LocalDateTime.parse(cdr[3], dateTimeFormatter))
                );
            });

        } catch (IOException e) {
            throw new FileCorruptedException("Ошибка при чтении файла. Проверьте пустые строки в cdr.txt и его расположение", new IOException());
        }

        return callDataRecordDtoList;
    }
}
