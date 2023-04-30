package ru.sladkkov.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.sladkkov.common", "ru.sladkkov.cdr"})
public class CdrApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdrApplication.class, args);
    }
}
