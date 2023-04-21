package ru.sladkkov.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sladkkov.common.dto.ChangeTariffDto;
import ru.sladkkov.common.dto.abonent.AbonentDto;
import ru.sladkkov.common.dto.abonent.mapper.AbonentMapper;
import ru.sladkkov.common.service.impl.ManagerServiceImpl;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerServiceImpl managerServiceImpl;
    private final AbonentMapper abonentMapper;

    @PatchMapping("/changeTariff")
    public ResponseEntity<ChangeTariffDto> changeTariff(ChangeTariffDto changeTariffDto) {
        return ResponseEntity.ok(managerServiceImpl.changeAbonentTariff(changeTariffDto));
    }

    @PostMapping("/abonent")
    public ResponseEntity<AbonentDto> saveAbonent(AbonentDto abonentDto) {
        return ResponseEntity.ok(abonentMapper.toDto(managerServiceImpl.createAbonent(abonentMapper.toEntity(abonentDto))));
    }
}
