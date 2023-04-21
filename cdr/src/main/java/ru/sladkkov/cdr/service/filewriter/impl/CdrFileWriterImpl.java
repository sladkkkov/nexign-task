package ru.sladkkov.cdr.service.filewriter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.filewriter.CdrFileWriter;
import ru.sladkkov.common.dto.CallDataRecordDto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Component
@RequiredArgsConstructor
public class CdrFileWriterImpl implements CdrFileWriter {

    @Value("${cdr.generate.path}")
    private String url;
    private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyyMMddHHmmss")
            .toFormatter();

    @Override
    public void writeFile(CallDataRecordDto callDataRecordDto) throws IOException {
        try (FileWriter fw = new FileWriter(url, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(cdrFormat(callDataRecordDto));
        }
    }

    private String cdrFormat(CallDataRecordDto callDataRecordDto) {

        return callDataRecordDto.getTypeCall().getNumericTypeCall() + ", " +
                callDataRecordDto.getAbonentNumber() + ", " +
                callDataRecordDto.getDateAndTimeStartCall().format(dateTimeFormatter) + ", " +
                callDataRecordDto.getDateAndTimeEndCall().format(dateTimeFormatter) + '\n';
    }
}
