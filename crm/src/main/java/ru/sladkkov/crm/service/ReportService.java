package ru.sladkkov.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.model.CallInfo;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.common.repository.CallInfoRepository;
import ru.sladkkov.crm.dto.CallDto;
import ru.sladkkov.crm.dto.Report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AbonentRepository abonentRepository;
    private final CallInfoRepository callInfoRepository;

    private static List<CallDto> getCallDtos(List<CallInfo> callInfoList) {
        List<CallDto> callDtoList = new ArrayList<>();

        for (CallInfo callInfo : callInfoList) {
            var callDto = CallDto.builder()
                    .typeCall(callInfo.getTypeCall().getCode())
                    .startTime(callInfo.getStartTime())
                    .endTime(callInfo.getEndTime())
                    .duration(callInfo.getDuration())
                    .cost(callInfo.getCost())
                    .build();

            callDtoList.add(callDto);
        }

        return callDtoList;
    }

    public Report getReportByAbonentNumber(String abonentNumber) {

        var abonent = abonentRepository.findByAbonentNumber(abonentNumber)
                .orElseThrow(() -> new AbonentNotFoundException("Номера не существует"));

        var callInfoList = callInfoRepository.findAllByAbonent(abonent);

        List<CallDto> callDtoList = getCallDtos(callInfoList);

        return Report.builder()
                .id(abonent.getId())
                .tariffIndex(abonent.getTariff().getTariffId())
                .abonentNumber(abonent.getAbonentNumber())
                .payments(callDtoList)
                .totalCost(callDtoList.stream().map(CallDto::getCost).reduce(BigDecimal.ZERO, BigDecimal::add))
                .monetaryUnit("RU")
                .build();
    }
}
