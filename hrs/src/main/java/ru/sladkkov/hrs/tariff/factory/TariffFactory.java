package ru.sladkkov.hrs.tariff.factory;

import org.springframework.stereotype.Component;
import ru.sladkkov.hrs.exception.TariffConvertationException;
import ru.sladkkov.hrs.tariff.factory.impl.MinuteTariff;
import ru.sladkkov.hrs.tariff.factory.impl.OrdinaryTariff;
import ru.sladkkov.hrs.tariff.factory.impl.UnlimitedTariff;
import ru.sladkkov.hrs.tariff.factory.impl.XTariff;

@Component
public final class TariffFactory {
    private TariffFactory() {
    }

    public static Tariffable createCdr(String typeTariff) {

        if (typeTariff.equals("06")) {
            return new UnlimitedTariff();
        }

        if (typeTariff.equals("03")) {
            return new MinuteTariff();
        }

        if (typeTariff.equals("11")) {
            return new OrdinaryTariff();
        }

        if (typeTariff.equals("82")) {
            return new XTariff();
        }

        throw new TariffConvertationException("Ошибка конвертации тарифа");
    }
}
