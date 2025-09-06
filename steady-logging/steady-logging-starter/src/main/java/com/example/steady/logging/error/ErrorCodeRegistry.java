package com.example.steady.logging.error;
public class ErrorCodeRegistry {
  public record Item(String code, int http, String category, String message){}
  private final java.util.Map<String, Item> byCode = new java.util.HashMap<>();
  public ErrorCodeRegistry(String resourcePath) {
    try (var in = java.util.Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(resourcePath),
        "Missing error-codes.yaml on classpath")) {
      var load = new org.snakeyaml.engine.v2.api.Load(org.snakeyaml.engine.v2.api.LoadSettings.builder().build());
      var root = (java.util.Map<String,Object>) load.loadFromInputStream(in);
      var codes = (java.util.List<java.util.Map<String,Object>>) root.get("codes");
      for (var c : codes) {
        var code = String.valueOf(c.get("code"));
        var http = Integer.parseInt(String.valueOf(c.get("http")));
        var cat = String.valueOf(c.get("category"));
        var msg = String.valueOf(c.get("message"));
        byCode.put(code, new Item(code, http, cat, msg));
      }
    } catch (Exception e) { throw new IllegalStateException("Cannot load error-codes.yaml", e); }
  }
  public java.util.Optional<Item> find(String code){ return java.util.Optional.ofNullable(byCode.get(code)); }
}
