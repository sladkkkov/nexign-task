package ru.sladkkov.brt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sladkkov.common.model.TypeCall;

import java.util.Optional;

public interface TypeCallRepository extends JpaRepository<TypeCall, Long> {
    Optional<TypeCall> findByCode(String code);
}