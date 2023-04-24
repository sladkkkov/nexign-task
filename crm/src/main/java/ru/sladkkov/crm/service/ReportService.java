package ru.sladkkov.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.dto.abonent.mapper.AbonentMapper;
import ru.sladkkov.common.exception.AbonentNotFoundException;
import ru.sladkkov.common.mapper.CallInfoMapper;
import ru.sladkkov.common.mapper.TypeCallMapper;
import ru.sladkkov.common.model.CallInfo;
import ru.sladkkov.common.repository.AbonentRepository;
import ru.sladkkov.common.repository.CallInfoRepository;
import ru.sladkkov.crm.dto.Report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AbonentRepository abonentRepository;
    private final CallInfoRepository callInfoRepository;
    private final CallInfoMapper callInfoMapper;
    private final AbonentMapper abonentMapper;
    private final TypeCallMapper typeCallMapper;

    public Report getReportByAbonentNumber(String abonentNumber) {

        var abonent = abonentRepository.findByAbonentNumber(abonentNumber)
                .orElseThrow(() -> new AbonentNotFoundException("Номера не существует"));

        var callInfoList = callInfoRepository.findAllByAbonent(abonent);

        List<CallInfoDto> callInfoDtoList = new ArrayList<>();

        for (CallInfo callInfo : callInfoList) {
            var callInfoDto = callInfoMapper.toDto(callInfo);
            callInfoDto.setTypeCall(typeCallMapper.toDto(callInfo.getTypeCall()));
            callInfoDto.setAbonent(abonentMapper.toDto(abonent));
            callInfoDtoList.add(callInfoDto);
        }

        return Report.builder()
                .id(abonent.getId())
                .tariffIndex(abonent.getTariff().getTariffId())
                .abonentNumber(abonent.getAbonentNumber())
                .payments(callInfoDtoList)
                .tootalCost(callInfoDtoList.stream().map(CallInfoDto::getCost).reduce(BigDecimal.ZERO, BigDecimal::add))
                .monetartUnit("RU")
                .build();
    }
}
