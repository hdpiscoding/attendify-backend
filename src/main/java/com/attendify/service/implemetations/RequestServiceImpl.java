package com.attendify.service.implemetations;

import com.attendify.dto.PaginatedResponseDTO;
import com.attendify.dto.RequestDTO;
import com.attendify.dto.RequestFilterDTO;
import com.attendify.entity.Request;
import com.attendify.entity.User;
import com.attendify.mapper.RequestMapper;
import com.attendify.repository.RequestRepository;
import com.attendify.repository.UserRepository;
import com.attendify.service.interfaces.RequestService;
import com.attendify.specification.RequestSpecification;
import com.attendify.utils.enums.RequestStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<RequestDTO> getRequests(RequestFilterDTO filter) {
        int page = filter.getPage() == null ? 1 : filter.getPage();
        int limit = filter.getLimit() == null ? 10 : filter.getLimit();
        Specification<Request> spec = Specification.where(null);

        if (filter.getUserId() != null && !filter.getUserId().toString().isEmpty()) {
            spec = spec.and(RequestSpecification.hasEmployeeId(filter.getUserId()));
        }

        if (filter.getStatus() != null && !filter.getStatus().toString().isEmpty()) {
            spec = spec.and(RequestSpecification.hasStatus(filter.getStatus()));
        }

        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Page<Request> requests = requestRepository.findAll(spec, pageRequest);
        List<RequestDTO> requestDTOs = requestMapper.toDtoList(requests.getContent());
        return new PaginatedResponseDTO<>(
                requestDTOs,
                requests.getNumber() + 1,
                requests.getSize(),
                requests.getTotalElements(),
                requests.getTotalPages(),
                requests.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDTO getRequest(UUID id) {
        return requestMapper.toDto(requestRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public RequestDTO createRequest(UUID userId, RequestDTO requestDTO) {
        Request request = requestMapper.toEntity(requestDTO);
        User user = userRepository.findById(userId).orElse(null);
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public void approveRequest(UUID requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be approved");
        }
        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);
    }

    @Override
    @Transactional
    public void rejectRequest(UUID requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be rejected");
        }
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
    }

    @Override
    @Transactional
    public void deleteRequest(UUID requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        requestRepository.delete(request);
    }
}
