package com.example.steady.logging.ai;
public class AiLoggingSupport {
  private final com.example.steady.logging.json.SafeLogger log;
  private final com.example.steady.logging.SteadyLoggingProperties props;
  public AiLoggingSupport(com.example.steady.logging.json.SafeLogger log, com.example.steady.logging.SteadyLoggingProperties props){
    this.log = log; this.props = props;
  }
  public record Pricing(double inputUsdPer1k, double outputUsdPer1k){}
  public double calcCostUsd(int inputTokens, int outputTokens, Pricing pricing) {
    double inK = inputTokens / 1000.0; double outK = outputTokens / 1000.0;
    return Math.round((inK*pricing.inputUsdPer1k + outK*pricing.outputUsdPer1k) * 1_000_000d)/1_000_000d;
  }
  public void logAiCall(String model, String promptRaw, int inTok, int outTok, double costUsd, boolean pass, java.util.List<String> violations) {
    String promptHash = com.example.steady.logging.crypto.HashingUtil.sha256(props.getHashing().getSalt(), promptRaw == null ? "" : promptRaw);
    log.aiCall(java.util.Map.of(
        "model", model,
        "prompt_hash", promptHash,
        "input_tokens", inTok,
        "output_tokens", outTok,
        "cost_usd", costUsd,
        "policy", java.util.Map.of("pass", pass, "violations", violations)
    ));
  }
}
