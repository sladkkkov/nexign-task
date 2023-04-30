package ru.sladkkov.crm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sladkkov.crm.dto.request.LoginRequest;
import ru.sladkkov.crm.dto.response.JwtResponse;
import ru.sladkkov.crm.model.Users;
import ru.sladkkov.crm.security.jwt.TokenProvider;


@Service
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }


    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Start authenticate users with: " + loginRequest.getUsername());
        String username = loginRequest.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
        Users users = userService.findByUsername(username);
        String token = tokenProvider.createJwtToken(username, users.getRoles());

        return JwtResponse.builder()
                .id(users.getId())
                .token(token)
                .username(username)
                .roles(users.getRoles())
                .build();
    }
}
