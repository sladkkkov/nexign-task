package ru.sladkkov.common.dto.tariff.mapper;

import org.mapstruct.*;
import ru.sladkkov.common.dto.tariff.TariffDto;
import ru.sladkkov.common.model.Tariff;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TariffMapper {
    Tariff toEntity(TariffDto tariffDto);

    TariffDto toDto(Tariff tariff);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Tariff partialUpdate(TariffDto tariffDto, @MappingTarget Tariff tariff);
}