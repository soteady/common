package com.example.steady.demo.web;
@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/ai")
public class AiController {
  private final com.example.steady.logging.ai.AiGuardService guard;
  private final com.example.steady.logging.ai.AiLoggingSupport aiLog;
  private final com.example.steady.logging.SteadyLoggingProperties props;
  public AiController(com.example.steady.logging.ai.AiGuardService guard, com.example.steady.logging.ai.AiLoggingSupport aiLog, com.example.steady.logging.SteadyLoggingProperties props) {
    this.guard = guard; this.aiLog = aiLog; this.props = props;
  }
  public record AiReq(String model, String prompt, int inputTokens, int outputTokens){}
  @org.springframework.web.bind.annotation.PostMapping("/completion")
  public org.springframework.http.ResponseEntity<?> completion(@org.springframework.web.bind.annotation.RequestBody AiReq req) {
    var policy = guard.check(req.prompt());
    var pricing = new com.example.steady.logging.ai.AiLoggingSupport.Pricing(0.00015, 0.0006);
    double cost = aiLog.calcCostUsd(req.inputTokens(), req.outputTokens(), pricing);
    aiLog.logAiCall(req.model(), req.prompt(), req.inputTokens(), req.outputTokens(), cost, policy.pass, policy.violations);
    if (!policy.pass) return org.springframework.http.ResponseEntity.status(400).body(java.util.Map.of("error","Denied by AI Guard","violations", policy.violations));
    return org.springframework.http.ResponseEntity.ok(java.util.Map.of("text","<<mocked completion>>", "cost_usd", cost));
  }
}
