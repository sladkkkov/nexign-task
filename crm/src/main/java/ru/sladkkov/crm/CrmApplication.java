package ru.sladkkov.crm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
@EnableWebSecurity
@EntityScan(basePackages = {"ru.sladkkov.common.model", "ru.sladkkov.crm.model"})
@ComponentScan(basePackages = {"ru.sladkkov.common", "ru.sladkkov.crm"})
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}
