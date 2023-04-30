package ru.sladkkov.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sladkkov.crm.dto.request.RegisterRequest;
import ru.sladkkov.crm.service.RegistrationService;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping(value = "api/v1/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegisterRequest registerRequest) throws RoleNotFoundException {
        registrationService.registration(registerRequest);
        return ResponseEntity.ok("Вы успешно зарегистрировались");
    }
}
