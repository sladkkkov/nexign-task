package ru.sladkkov.common.dto.abonent.mapper;


import org.mapstruct.Mapper;
import ru.sladkkov.common.model.Abonent;

@Mapper(componentModel = "spring")
public interface AbonentMapper {

    ru.sladkkov.common.dto.abonent.AbonentDto toDto(Abonent abonent);

    Abonent toModel(ru.sladkkov.common.dto.abonent.AbonentDto abonentDto);
}
