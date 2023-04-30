package ru.sladkkov.crm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sladkkov.crm.dto.request.RegisterRequest;
import ru.sladkkov.crm.exception.UserAlreadyCreatedException;
import ru.sladkkov.crm.model.RoleEnum;
import ru.sladkkov.crm.model.Users;
import ru.sladkkov.crm.repository.RoleRepository;
import ru.sladkkov.crm.repository.UserRepository;


import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registration(RegisterRequest registerRequest) throws RoleNotFoundException {
        log.info("Users with username: " + registerRequest.getUsername() + " start registration");
        Users users = new Users(registerRequest.getUsername(), encodePassword(registerRequest), registerRequest.getFirstName(), registerRequest.getLastName());

        if (userRepository.findByUsername(registerRequest.getUsername()).isEmpty()) {
            setUserRole(users);
            userRepository.save(users);
            log.info("Users: " + registerRequest.getUsername() + " successfully created");

        } else {
            log.error("Users: " + registerRequest.getUsername() + " is already created");
            throw new UserAlreadyCreatedException("Пользователь с таким логином: " + registerRequest.getUsername() + " уже существует");
        }
    }

    private String encodePassword(RegisterRequest registerRequest) {
        return bCryptPasswordEncoder.encode(registerRequest.getPassword());
    }

    private void setUserRole(Users users) throws RoleNotFoundException {
        users.setRoles(List.of(roleRepository.findByName(RoleEnum.USER).orElseThrow(() -> new RoleNotFoundException("Такой роли не существует"))));
        log.info("Successfully set role to Users");
    }
}
