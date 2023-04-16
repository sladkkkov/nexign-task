package ru.sladkkov.cdr.enums;


import ru.sladkkov.cdr.exception.TariffConvertationException;

import java.util.List;

public enum TypeTariff {
    UNLIMITED("06"),
    MINUTE("03"),
    ORDINARY("11");

    public static final List<TypeTariff> TYPE_TARIFFS = List.of(TypeTariff.values());
    private final String numericTypeTariff;

    TypeTariff(String numericTypeTariff) {
        this.numericTypeTariff = numericTypeTariff;
    }

    public static TypeTariff fromNumericRateOfType(String numericTypeRate) {

        if (numericTypeRate.equals("06")) {
            return UNLIMITED;
        }

        if (numericTypeRate.equals("03")) {
            return MINUTE;
        }

        if (numericTypeRate.equals("11")) {
            return ORDINARY;
        }

        throw new TariffConvertationException("Неопознаное значение: " + numericTypeRate, new IllegalArgumentException());
    }

    public String getNumericTypeTariff() {
        return numericTypeTariff;
    }
}
