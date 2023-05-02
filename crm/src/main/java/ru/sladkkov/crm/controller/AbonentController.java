package ru.sladkkov.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sladkkov.common.dto.PaymentDto;
import ru.sladkkov.crm.dto.Report;
import ru.sladkkov.crm.service.ReportService;
import ru.sladkkov.crm.service.impl.AbonentServiceImpl;

@RestController
@RequestMapping("/api/v1/abonent")
@RequiredArgsConstructor
public class AbonentController {

    private final AbonentServiceImpl abonentServiceImpl;
    private final ReportService reportService;

    @GetMapping("report/{abonentNumber}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Report> getReport(@PathVariable String abonentNumber) {
        return ResponseEntity.ok(reportService.getReportByAbonentNumber(abonentNumber));
    }

    @PatchMapping("/pay")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<PaymentDto> pay(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(abonentServiceImpl.pay(paymentDto));
    }
}


