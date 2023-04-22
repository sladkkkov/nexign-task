package ru.sladkkov.common.mapper;

import org.mapstruct.*;
import ru.sladkkov.common.dto.TypeCallDto;
import ru.sladkkov.common.model.TypeCall;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TypeCallMapper {
    TypeCall toEntity(TypeCallDto typeCallDto);

    TypeCallDto toDto(TypeCall typeCall);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TypeCall partialUpdate(TypeCallDto typeCallDto, @MappingTarget TypeCall typeCall);
}