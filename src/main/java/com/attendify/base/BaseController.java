package com.attendify.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected ResponseEntity<Object> buildResponse(Object data, HttpStatus status, String message) {
        logger.info("Response: Status={}, Data={}", status, data);

        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        response.put("data", data);

        return ResponseEntity.status(status).body(response);
    }
}
