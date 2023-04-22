package ru.sladkkov.hrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.sladkkov.common", "ru.sladkkov.hrs"})
public class HrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrsApplication.class, args);
    }

}
