package ru.sladkkov.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sladkkov.common.dto.PaymentDto;
import ru.sladkkov.crm.service.ReportService;
import ru.sladkkov.crm.service.impl.AbonentServiceImpl;

@RestController
@RequestMapping("/api/v1/abonent")
@RequiredArgsConstructor
public class AbonentController {

    private final AbonentServiceImpl abonentServiceImpl;
    private final ReportService reportService;

    @GetMapping("report/{abonentNumber}")
    public void getReport(@PathVariable String abonentNumber) {
        reportService.getReportByAbonentNumber(abonentNumber);
    }

    @PatchMapping("/pay")
    public ResponseEntity<PaymentDto> pay(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(abonentServiceImpl.pay(paymentDto));
    }
}


