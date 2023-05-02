package ru.sladkkov.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ru.sladkkov.common.service.impl.ManagerServiceImpl;
import ru.sladkkov.common.dto.ChangeTariffDto;
import ru.sladkkov.common.dto.abonent.AbonentDto;
import ru.sladkkov.common.dto.abonent.mapper.AbonentMapper;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerServiceImpl managerServiceImpl;
    private final AbonentMapper abonentMapper;

    @PatchMapping("/changeTariff")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ChangeTariffDto> changeTariff(@RequestBody ChangeTariffDto changeTariffDto) {
        return ResponseEntity.ok(managerServiceImpl.changeAbonentTariff(changeTariffDto));
    }

    @PostMapping("/abonent")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<AbonentDto> saveAbonent(AbonentDto abonentDto) {
        return ResponseEntity.ok(abonentMapper.toDto(managerServiceImpl.createAbonent(abonentMapper.toEntity(abonentDto))));
    }

}
