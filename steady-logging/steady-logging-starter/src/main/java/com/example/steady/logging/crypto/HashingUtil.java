package com.example.steady.logging.crypto;
public final class HashingUtil {
  private HashingUtil(){}
  public static String sha256(String salt, String raw) {
    try {
      var md = java.security.MessageDigest.getInstance("SHA-256");
      md.update(salt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      byte[] digest = md.digest(raw == null ? new byte[0] : raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      return java.util.HexFormat.of().formatHex(digest);
    } catch (Exception e) { return "sha256_error"; }
  }
}
