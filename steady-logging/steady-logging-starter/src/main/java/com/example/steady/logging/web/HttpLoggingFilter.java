//package com.example.steady.logging.web;
//
//public class HttpLoggingFilter extends org.springframework.web.filter.OncePerRequestFilter {
//    private final com.example.steady.logging.json.SafeLogger log;
//
//    public HttpLoggingFilter(com.example.steady.logging.json.SafeLogger log) {
//        this.log = log;
//    }
//
//    @Override
//    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse res, jakarta.servlet.FilterChain chain)
//            throws jakarta.servlet.ServletException, java.io.IOException {
//        String traceId = req.getHeader("traceparent");
//        if (traceId == null) traceId = com.example.steady.logging.trace.Trace.genTraceId();
//        String spanId = com.example.steady.logging.trace.Trace.genSpanId();
//        var start = java.time.Instant.now();
//        try {
//            chain.doFilter(req, res);
//        } finally {
//            long dur = java.time.Duration.between(start, java.time.Instant.now()).toMillis();
//            String ua = req.getHeader("User-Agent");
//            String clientIp = (req.getHeader("X-Forwarded-For") != null)
//                    ? req.getHeader("X-Forwarded-For").split(",")[0].trim()
//                    : req.getRemoteAddr();
//            log.http(java.util.Map.of(
//                    "method", req.getMethod(),
//                    "path", req.getRequestURI(),
//                    "status", res.getStatus(),
//                    "duration_ms", dur,
//                    "client_ip", clientIp,
//                    "user_agent", ua == null ? "" : ua
//            ), res.getStatus() >= 500 ? "ERROR" : "INFO", traceId, spanId);
//        }
//    }
//}
package com.example.steady.logging.web;

public class HttpLoggingFilter extends org.springframework.web.filter.OncePerRequestFilter {
    private final com.example.steady.logging.json.SafeLogger log;
    public HttpLoggingFilter(com.example.steady.logging.json.SafeLogger log){ this.log = log; }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest req,
                                    jakarta.servlet.http.HttpServletResponse res,
                                    jakarta.servlet.FilterChain chain)
            throws jakarta.servlet.ServletException, java.io.IOException {

        // Đặt cờ steady trong MDC để mọi log của request này có thể filter
        org.slf4j.MDC.put("steady", "1");          // <-- giá trị bạn muốn filter (vd "1" hoặc "true")

        String traceId = req.getHeader("traceparent");
        if (traceId == null) traceId = com.example.steady.logging.trace.Trace.genTraceId();
        String spanId = com.example.steady.logging.trace.Trace.genSpanId();
        var start = java.time.Instant.now();

        try {
            chain.doFilter(req, res);
        } finally {
            long dur = java.time.Duration.between(start, java.time.Instant.now()).toMillis();
            String ua = req.getHeader("User-Agent");
            String clientIp = (req.getHeader("X-Forwarded-For") != null)
                    ? req.getHeader("X-Forwarded-For").split(",")[0].trim()
                    : req.getRemoteAddr();

            log.http(java.util.Map.of(
                    "method", req.getMethod(),
                    "path", req.getRequestURI(),
                    "status", res.getStatus(),
                    "duration_ms", dur,
                    "client_ip", clientIp,
                    "user_agent", ua == null ? "" : ua
            ), res.getStatus() >= 500 ? "ERROR" : "INFO", traceId, spanId);

            // Dọn MDC tránh rò rỉ sang thread reuse
            org.slf4j.MDC.remove("steady");
        }
    }
}
