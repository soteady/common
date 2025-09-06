package com.example.steady.logging.error;
@org.springframework.web.bind.annotation.RestControllerAdvice
public class ErrorAdvice {
  private final ErrorCodeRegistry registry;
  private final com.example.steady.logging.json.SafeLogger safeLogger;
  public ErrorAdvice(ErrorCodeRegistry registry, com.example.steady.logging.json.SafeLogger safeLogger) {
    this.registry = registry; this.safeLogger = safeLogger;
  }
  @org.springframework.web.bind.annotation.ExceptionHandler(ErrorCodeException.class)
  public org.springframework.http.ResponseEntity<java.util.Map<String,Object>> onErrorCode(ErrorCodeException ex) {
    var item = registry.find(ex.getCode())
        .orElse(new ErrorCodeRegistry.Item(ex.getCode(), 500, "SYSTEM", ex.getMessage()));
    safeLogger.bizEvent(java.util.Map.of("event","error",
        "error", java.util.Map.of("code", item.code(), "safe_message", item.message())));
    return org.springframework.http.ResponseEntity.status(item.http()).body(java.util.Map.of(
        "error", java.util.Map.of("code", item.code(),"message", item.message())));
  }
  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public org.springframework.http.ResponseEntity<java.util.Map<String,Object>> onOther(Exception ex) {
    safeLogger.bizEvent(java.util.Map.of("event","error","error",
        java.util.Map.of("code","E-SYS-001","safe_message","Unexpected error")));
    return org.springframework.http.ResponseEntity.internalServerError().body(java.util.Map.of("error","Unexpected"));
  }
}
