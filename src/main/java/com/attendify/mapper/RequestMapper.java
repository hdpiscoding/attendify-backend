package com.attendify.mapper;

import com.attendify.base.BaseMapper;
import com.attendify.dto.RequestDTO;
import com.attendify.entity.Request;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RequestMapper implements BaseMapper<Request, RequestDTO> {
    private final ModelMapper modelMapper;


    @Override
    public RequestDTO toDto(Request entity) {
        return modelMapper.map(entity, RequestDTO.class);
    }

    @Override
    public Request toEntity(RequestDTO dto) {
        return modelMapper.map(dto, Request.class);
    }

    @Override
    public List<RequestDTO> toDtoList(List<Request> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Request> toEntityList(List<RequestDTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<RequestDTO> toDtoSet(Set<Request> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<Request> toEntitySet(Set<RequestDTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
