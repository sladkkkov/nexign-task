package ru.sladkkov.brt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.sladkkov.common")
public class BrtApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrtApplication.class, args);
    }
}
