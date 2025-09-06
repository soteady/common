package com.example.steady.logging.ai;
public class AiGuardService {
  private final com.example.steady.logging.SteadyLoggingProperties props;
  private final java.util.List<java.util.regex.Pattern> sensitive = java.util.List.of(
          java.util.regex.Pattern.compile("(?i)password\\s*[:=]"),
          java.util.regex.Pattern.compile("(?i)api[_-]?key"),
          java.util.regex.Pattern.compile("(?i)secret"),
          java.util.regex.Pattern.compile("(?s)BEGIN\\s+RSA\\s+PRIVATE"),
          java.util.regex.Pattern.compile("(?i)bearer\\s+[a-z0-9\\-._~]+")
  );

  public AiGuardService(com.example.steady.logging.SteadyLoggingProperties props){ this.props = props; }
  public static final class Policy { public final boolean pass; public final java.util.List<String> violations;
    public Policy(boolean pass, java.util.List<String> v){ this.pass = pass; this.violations = v; } }
  public Policy check(String prompt) {
    var v = new java.util.ArrayList<String>();
    for (var p : sensitive) if (p.matcher(prompt == null ? "" : prompt).find()) v.add("SENSITIVE_TOKEN");
    boolean pass = v.isEmpty() || !"deny-on-sensitive".equalsIgnoreCase(props.getAi().getGuardMode());
    return new Policy(pass, v);
  }
}
