package ru.sladkkov.dtoshare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sladkkov.dtoshare.enums.TypeTariff;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallDataRecordPlusDto {

    private CallDataRecordDto callDataRecordDto;

    private TypeTariff tariff;
}
