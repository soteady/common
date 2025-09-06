package com.example.steady.demo.web;

@org.springframework.web.bind.annotation.RestController
public class OrderController {
    private final com.example.steady.logging.json.SafeLogger log;

    public OrderController(com.example.steady.logging.json.SafeLogger log) {
        this.log = log;
    }

    public record OrderRequest(String orderId, String email, String phone, String card, String account, double amount) {
    }

    @org.springframework.web.bind.annotation.PostMapping("/order")
    public java.util.Map<String, Object> create(@org.springframework.web.bind.annotation.RequestBody OrderRequest req) {
        log.bizEvent(java.util.Map.of("biz", java.util.Map.of("order_id", req.orderId(), "amount", req.amount())));
        return java.util.Map.of("status", "OK");
    }

    @org.springframework.web.bind.annotation.GetMapping("/error/{code}")
    public void error(@org.springframework.web.bind.annotation.PathVariable String code) {
        if ("E-ORD-001".equals(code))
            throw new com.example.steady.logging.error.ErrorCodeException("E-ORD-001", "Order not found");
        if ("E-AUTH-001".equals(code))
            throw new com.example.steady.logging.error.ErrorCodeException("E-AUTH-001", "Unauthorized");
        throw new com.example.steady.logging.error.ErrorCodeException("E-SYS-001", "Unexpected error");
    }
}
