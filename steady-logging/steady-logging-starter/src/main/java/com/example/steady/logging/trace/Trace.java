package com.example.steady.logging.trace;
public final class Trace {
  private static final java.security.SecureRandom RND = new java.security.SecureRandom();
  private Trace(){}
  public static String genTraceId() { return hex(16); }
  public static String genSpanId()  { return hex(8);  }
  private static String hex(int bytes) {
    byte[] b = new byte[bytes]; RND.nextBytes(b);
    var sb = new StringBuilder(bytes * 2);
    for (byte value : b) sb.append(String.format("%02x", value));
    return sb.toString();
  }
}
