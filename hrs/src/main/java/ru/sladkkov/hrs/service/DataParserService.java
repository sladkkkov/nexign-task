package ru.sladkkov.hrs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallDataRecordDto;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;
import ru.sladkkov.common.dto.TypeCallDto;
import ru.sladkkov.common.dto.tariff.TariffDto;
import ru.sladkkov.common.exception.FileCorruptedException;
import ru.sladkkov.hrs.exception.CdrParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс отвечающий за полученние данных cdrPlus из файла, сформированного в модуле brt
 *
 * @author Danila Sladkov
 */
@Service
public class DataParserService {

    /**
     * DateTimeFormatter yyyy-MM-dd-HH-mm-ss, используется, чтобы парсить дату в нужный формат из файла.
     */
    private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMddHHmmss")
            .toFormatter();

    /**
     * Путь по которому необходимо читать файл, записанный в модуле brt.
     */
    @Value("${cdr.generate.plus-cdr-path}")
    private String url;

    /**
     * Функция получения List<CallDataRecordPlusDto> из файла сформированного в модуле brt
     *
     * @return List<CallDataRecordPlusDto>
     */
    public List<CallDataRecordPlusDto> getCallDataRecordDtoFromFile() {

        List<CallDataRecordPlusDto> callDataRecordDtoList = new ArrayList<>();
        var path = Paths.get(url);

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(t -> {
                var cdr = t.split(", ");

                if (cdr.length != 14) {
                    throw new CdrParseException("Длина cdr должна быть 14, проверьте данные.");
                }

                callDataRecordDtoList.add(createCallDataRecordPlusDto(cdr));
            });

        } catch (IOException e) {
            throw new FileCorruptedException("Ошибка при чтении файла. Проверьте пустые строки в cdr.txt и его расположение", new IOException());
        }

        return callDataRecordDtoList;
    }

    private CallDataRecordPlusDto createCallDataRecordPlusDto(String[] cdr) {
        return new CallDataRecordPlusDto(
                CallDataRecordDto.builder()
                        .typeCall(TypeCallDto.builder().code(cdr[0]).build())
                        .abonentNumber(cdr[1])
                        .dateAndTimeStartCall(LocalDateTime.parse(cdr[2], dateTimeFormatter))
                        .dateAndTimeEndCall(LocalDateTime.parse(cdr[3], dateTimeFormatter))
                        .build(),

                TariffDto.builder()
                        .tariffId(cdr[4])
                        .name(cdr[5])
                        .fixedPrice(new BigDecimal(cdr[6]))
                        .freeMinuteForFixedPrice(Integer.valueOf(cdr[7]))
                        .priceForMinute(new BigDecimal(cdr[8]))
                        .actionMinute(Integer.valueOf(cdr[9]))
                        .actionPrice(new BigDecimal(cdr[10]))
                        .incomingFree(Boolean.valueOf(cdr[11]))
                        .outgoingFree(Boolean.valueOf(cdr[12]))
                        .freeInsideNetwork(Boolean.valueOf(cdr[13]))
                        .build());
    }
}
