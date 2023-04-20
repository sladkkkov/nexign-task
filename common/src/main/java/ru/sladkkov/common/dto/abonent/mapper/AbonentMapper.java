package ru.sladkkov.common.dto.abonent.mapper;


import org.mapstruct.Mapper;
import ru.sladkkov.common.dto.abonent.AbonentDto;
import ru.sladkkov.common.model.Abonent;

@Mapper(componentModel = "spring")
public interface AbonentMapper {

    AbonentDto toDto(Abonent abonent);

    Abonent toModel(AbonentDto abonentDto);
}
