package com.example.steady.logging;
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "steady.logging")
public class SteadyLoggingProperties {
  private String service = "unknown-service";
  private String env = "local";
  private Masking masking = new Masking();
  private Hashing hashing = new Hashing();
  private Ai ai = new Ai();
  public static class Masking {
    private boolean enabled = true;
    private java.util.List<String> patterns = java.util.List.of(
            "(?i)([a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,})",
            "(?i)\\b(0|\\+?84)\\d{8,11}\\b",
            "\\b\\d{16}\\b",
            "\\b\\d{10,14}\\b",
            "(?i)bearer\\s+[a-z0-9\\-.~_]+",
            "(?i)api[_-]?key\\s*[:=]\\s*[a-z0-9\\-]{10,}",
            "(?s)BEGIN\\s+RSA\\s+PRIVATE\\s+KEY.*END\\s+RSA\\s+PRIVATE\\s+KEY"
    );
    private String maskWith = "****";
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}
    public java.util.List<String> getPatterns() {return patterns;}
    public void setPatterns(java.util.List<String> patterns) {this.patterns = patterns;}
    public String getMaskWith() {return maskWith;}
    public void setMaskWith(String maskWith) {this.maskWith = maskWith;}
  }
  public static class Hashing {
    private boolean enabled = true;
    private String salt = "change-me-in-env";
    public boolean isEnabled() {return enabled;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}
    public String getSalt() {return salt;}
    public void setSalt(String salt) {this.salt = salt;}
  }
  public static class Ai {
    private String guardMode = "deny-on-sensitive";
    public String getGuardMode() {return guardMode;}
    public void setGuardMode(String guardMode) {this.guardMode = guardMode;}
  }
  public String getService() {return service;}
  public void setService(String service) {this.service = service;}
  public String getEnv() {return env;}
  public void setEnv(String env) {this.env = env;}
  public Masking getMasking() {return masking;}
  public void setMasking(Masking masking) {this.masking = masking;}
  public Hashing getHashing() {return hashing;}
  public void setHashing(Hashing hashing) {this.hashing = hashing;}
  public Ai getAi() {return ai;}
  public void setAi(Ai ai) {this.ai = ai;}
}
