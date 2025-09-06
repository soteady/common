package com.example.steady.demo.web;

import org.slf4j.MDC;

@org.springframework.web.bind.annotation.RestController
public class HealthController {
    @org.springframework.web.bind.annotation.GetMapping("/health")
    public java.util.Map<String, Object> health() {
        MDC.put("steady","tenantA");
        return java.util.Map.of("status", "OK");
    }
}
