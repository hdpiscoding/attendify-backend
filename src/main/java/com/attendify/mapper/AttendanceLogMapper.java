package com.attendify.mapper;

import com.attendify.base.BaseMapper;
import com.attendify.dto.AttendanceLogDTO;
import com.attendify.entity.AttendanceLog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AttendanceLogMapper implements BaseMapper<AttendanceLog, AttendanceLogDTO> {
    private final ModelMapper modelMapper;

    @Override
    public AttendanceLogDTO toDto(AttendanceLog entity) {
        return modelMapper.map(entity, AttendanceLogDTO.class);
    }

    @Override
    public AttendanceLog toEntity(AttendanceLogDTO dto) {
        return modelMapper.map(dto, AttendanceLog.class);
    }

    @Override
    public List<AttendanceLogDTO> toDtoList(List<AttendanceLog> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceLog> toEntityList(List<AttendanceLogDTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Set<AttendanceLogDTO> toDtoSet(Set<AttendanceLog> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<AttendanceLog> toEntitySet(Set<AttendanceLogDTO> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
