package ru.sladkkov.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.sladkkov.common.dto.TypeCallDto;
import ru.sladkkov.common.model.TypeCall;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TypeCallMapper {
    TypeCall toEntity(TypeCallDto typeCallDto);

    TypeCallDto toDto(TypeCall typeCall);

}
