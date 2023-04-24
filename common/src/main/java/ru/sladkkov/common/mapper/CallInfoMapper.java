package ru.sladkkov.common.mapper;

import org.mapstruct.*;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.model.CallInfo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CallInfoMapper {
    CallInfo toEntity(CallInfoDto callInfoDto);

    CallInfoDto toDto(CallInfo callInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CallInfo partialUpdate(CallInfoDto callInfoDto, @MappingTarget CallInfo callInfo);
}