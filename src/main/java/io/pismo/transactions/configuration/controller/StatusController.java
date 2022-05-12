package io.pismo.transactions.configuration.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    private final Logger log = LoggerFactory.getLogger(StatusController.class);

    @Operation(summary = "for requesting the api status")
    @GetMapping("status")
    public ResponseEntity<Map<String, Object>> getApiStatus() {
        Map<String, Object> response = Map.of(
                "service", "transactions-api",
                "status", "up",
                "httpStatus", HttpStatus.OK.value()
        );
        log.info("requesting the api status");
        return ResponseEntity.ok(response);
    }

}
