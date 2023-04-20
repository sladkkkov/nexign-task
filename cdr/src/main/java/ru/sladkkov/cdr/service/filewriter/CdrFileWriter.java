package ru.sladkkov.cdr.service.filewriter;

import ru.sladkkov.common.dto.CallDataRecordDto;

import java.io.IOException;

public interface CdrFileWriter {

    void writeFile(CallDataRecordDto callDataRecordDto) throws IOException;
}
