package ru.sladkkov.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.sladkkov.common.dto.CallInfoDto;
import ru.sladkkov.common.model.CallInfo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CallInfoMapper {
    CallInfo toEntity(CallInfoDto callInfoDto);

    CallInfoDto toDto(CallInfo callInfo);

}
