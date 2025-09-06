package com.example.steady.logging.error;
public class ErrorCodeException extends RuntimeException {
  private final String code;
  public ErrorCodeException(String code, String safeMessage){ super(safeMessage); this.code = code; }
  public String getCode(){ return code; }
}
