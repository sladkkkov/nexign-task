package ru.sladkkov.brt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sladkkov.common.model.CallInfo;

public interface CallInfoRepository extends JpaRepository<CallInfo, Long> {
}