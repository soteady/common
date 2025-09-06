package com.example.steady.demo.web;
@org.springframework.web.bind.annotation.RestController
public class SlowController {
  @org.springframework.web.bind.annotation.GetMapping("/slow")
  public String slow(@org.springframework.web.bind.annotation.RequestParam(defaultValue = "250") long ms) throws InterruptedException {
    Thread.sleep(Math.max(0, ms)); return "slept " + ms + " ms";
  }
}
