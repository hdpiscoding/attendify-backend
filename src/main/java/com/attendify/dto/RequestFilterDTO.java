package com.attendify.dto;

import com.attendify.utils.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestFilterDTO {
    private Integer page;
    private Integer limit;
    private RequestStatus status;
    private UUID userId;
}
