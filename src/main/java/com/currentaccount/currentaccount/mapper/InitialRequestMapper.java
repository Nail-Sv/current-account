package com.currentaccount.currentaccount.mapper;

import com.currentaccount.currentaccount.dto.InitialRequestDTO;
import com.currentaccount.currentaccount.model.InitialRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InitialRequestMapper {
    InitialRequest toEntity(InitialRequestDTO initialRequestDTO);

    InitialRequestDTO toDto(InitialRequest initialRequest);

}
