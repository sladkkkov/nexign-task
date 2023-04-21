package ru.sladkkov.cdr.service.datagenerator;

import java.util.List;

public interface AbonentNumberGenerator {
    List<String> generateAbonentNumber();

    List<String> getNumbers();
}
