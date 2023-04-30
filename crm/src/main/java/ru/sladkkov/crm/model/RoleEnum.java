package ru.sladkkov.crm.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum RoleEnum implements GrantedAuthority {
    USER("USER"), ADMIN("ADMIN");
    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
