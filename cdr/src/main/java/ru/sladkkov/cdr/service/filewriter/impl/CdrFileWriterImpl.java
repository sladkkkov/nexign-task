package ru.sladkkov.cdr.service.filewriter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sladkkov.cdr.service.filewriter.CdrFileWriter;
import ru.sladkkov.common.dto.CallDataRecordDto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CdrFileWriterImpl implements CdrFileWriter {

    @Value("${cdr.generate.path}")
    private String url;

    @Override
    public void writeFile(CallDataRecordDto callDataRecordDto) throws IOException {
        try (FileWriter fw = new FileWriter(url, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(callDataRecordDto.toString());
            bw.newLine();

        }
    }
}
