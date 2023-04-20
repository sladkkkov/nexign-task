package ru.sladkkov.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallDataRecordPlusDto {

    private CallDataRecordDto callDataRecordDto;

    private TariffDto tariff;
}
