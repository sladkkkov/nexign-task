package ru.sladkkov.brt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sladkkov.brt.model.Abonent;

@Repository
public interface AbonentRepository extends JpaRepository<Abonent, Long> {

    Abonent findAbonentByTelephoneNumber(String telephoneNumber);
}
