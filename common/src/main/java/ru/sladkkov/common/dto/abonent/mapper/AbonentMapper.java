package ru.sladkkov.common.dto.abonent.mapper;

import org.mapstruct.*;
import ru.sladkkov.common.dto.abonent.AbonentDto;
import ru.sladkkov.common.dto.tariff.mapper.TariffMapper;
import ru.sladkkov.common.model.Abonent;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = TariffMapper.class)
public interface AbonentMapper {
    Abonent toEntity(AbonentDto abonentDto);

    AbonentDto toDto(Abonent abonent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Abonent partialUpdate(AbonentDto abonentDto, @MappingTarget Abonent abonent);
}