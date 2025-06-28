package com.attendify.specification;

import com.attendify.entity.Request;
import com.attendify.utils.enums.RequestStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class RequestSpecification {
    public static Specification<Request> hasStatus(RequestStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Request> hasEmployeeId(UUID employeeId) {
        return (root, query, cb) ->
                employeeId == null ? null : cb.equal(root.get("employee").get("id"), employeeId);
    }
}
