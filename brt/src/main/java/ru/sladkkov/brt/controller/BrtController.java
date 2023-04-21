package ru.sladkkov.brt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sladkkov.brt.service.BrtService;

import java.io.IOException;

@RestController
@RequestMapping("/v1/brt")
@RequiredArgsConstructor
public class BrtController {

    private final BrtService brtService;

    @PatchMapping("/runq")
    public ResponseEntity runf() throws IOException {
        brtService.run();
        return ResponseEntity.ok(1);
    }
}
