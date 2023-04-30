package ru.sladkkov.crm.model.userdetails;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.sladkkov.crm.model.Users;

import java.util.List;
import java.util.stream.Collectors;

public final class UserDetailsImplFactory {

    private UserDetailsImplFactory() {
    }

    public static UserDetailsImpl build(Users users) {
        return new UserDetailsImpl(
                users.getId(),
                users.getUsername(),
                users.getPassword(),
                getAuthorities(users));
    }

    private static List<SimpleGrantedAuthority> getAuthorities(Users users) {
        return users.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
}

