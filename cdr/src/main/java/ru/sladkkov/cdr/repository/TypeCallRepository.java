package ru.sladkkov.cdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sladkkov.common.model.TypeCall;

public interface TypeCallRepository extends JpaRepository<TypeCall, Long> {
}
