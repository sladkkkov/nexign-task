package ru.sladkkov.brt.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sladkkov.common.dto.CallDataRecordPlusDto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Component
public class CdrPlusFileWriter {
    private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMddHHmmss")
            .toFormatter();
    @Value("${cdr.generate.plus-cdr-path}")
    private String url;

    public void writeFile(CallDataRecordPlusDto callDataRecordPlusDto) throws IOException {
        try (FileWriter fw = new FileWriter(url, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(cdrFormat(callDataRecordPlusDto));
        }
    }

    private String cdrFormat(CallDataRecordPlusDto callDataRecordPlusDto) {

        var tariff = callDataRecordPlusDto.getTariff();
        var callDataRecordDto = callDataRecordPlusDto.getCallDataRecordDto();

        return callDataRecordDto.getTypeCall().getCode() + ", " +
                callDataRecordDto.getAbonentNumber() + ", " +
                callDataRecordDto.getDateAndTimeStartCall().format(dateTimeFormatter) + ", " +
                callDataRecordDto.getDateAndTimeEndCall().format(dateTimeFormatter) + ", " +
                tariff.getTariffId() + ", " +
                tariff.getName() + ", " +
                tariff.getFixedPrice() + ", " +
                tariff.getFreeMinuteForFixedPrice() + ", " +
                tariff.getPriceForMinute() + ", " +
                tariff.getActionMinute() + ", " +
                tariff.getActionPrice() + ", " +
                tariff.getIncomingFree() + ", " +
                tariff.getOutgoingFree() + ", " +
                tariff.getFreeInsideNetwork() + '\n';

    }
}
