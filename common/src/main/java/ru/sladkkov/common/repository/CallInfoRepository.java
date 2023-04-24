package ru.sladkkov.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sladkkov.common.model.Abonent;
import ru.sladkkov.common.model.CallInfo;

import java.util.List;

public interface CallInfoRepository extends JpaRepository<CallInfo, Long> {
    List<CallInfo> findAllByAbonent(Abonent abonent);

}
