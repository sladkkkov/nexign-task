package ru.sladkkov.crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sladkkov.crm.dto.payment.PaymentDto;
import ru.sladkkov.crm.dto.payment.mapper.PaymentMapper;
import ru.sladkkov.crm.service.impl.AbonentServiceImpl;

@RestController
@RequestMapping("/api/v1/abonent")
@RequiredArgsConstructor
public class AbonentController {

    private final AbonentServiceImpl abonentServiceImpl;
    private final PaymentMapper paymentMapper;

    @GetMapping("report/{numberPhone}")
    public void getReport(@PathVariable String numberPhone) {

    }

    @PatchMapping("/pay")
    public ResponseEntity<PaymentDto> pay(PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentMapper.toDto(abonentServiceImpl.pay(paymentMapper.toModel(paymentDto))));
    }
}


