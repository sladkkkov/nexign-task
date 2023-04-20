package ru.sladkkov.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sladkkov.common.model.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
}
