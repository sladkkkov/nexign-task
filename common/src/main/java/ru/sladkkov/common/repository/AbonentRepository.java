package ru.sladkkov.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sladkkov.common.model.Abonent;

import java.util.Optional;


@Repository
public interface AbonentRepository extends JpaRepository<Abonent, Long> {

    Optional<Abonent> findByAbonentNumber(String abonentNumber);
}
