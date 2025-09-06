package com.example.steady.logging.mask;
public final class MaskingUtil {
  private final java.util.List<java.util.regex.Pattern> patterns;
  private final String maskWith;
  public MaskingUtil(java.util.List<String> regexes, String maskWith) {
    this.patterns = regexes.stream().map(java.util.regex.Pattern::compile).toList();
    this.maskWith = maskWith;
  }
  public String mask(String input) {
    if (input == null) return null;
    String out = input;
    for (var p : patterns) out = p.matcher(out).replaceAll(maskWith);
    return out;
  }
}
