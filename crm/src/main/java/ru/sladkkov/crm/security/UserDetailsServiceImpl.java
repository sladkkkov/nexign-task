package ru.sladkkov.crm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sladkkov.crm.model.Users;
import ru.sladkkov.crm.model.userdetails.UserDetailsImplFactory;
import ru.sladkkov.crm.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        Users users = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Users with username: %s not found", username)));

        return UserDetailsImplFactory.build(users);
    }
}
