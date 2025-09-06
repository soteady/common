//package com.example.steady.logging.json;
//
//import com.example.steady.logging.SteadyLoggingProperties;
//import com.example.steady.logging.mask.MaskingUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class SafeLogger {
//  private final Logger logger = LoggerFactory.getLogger("steady");
//  private final MaskingUtil masking;
//  private final SteadyLoggingProperties props;
//  private final ObjectMapper om = new ObjectMapper();
//  private final String host;
//
//  public SafeLogger(MaskingUtil masking, SteadyLoggingProperties props) {
//    this.masking = masking; this.props = props;
//    String h; try { h = java.net.InetAddress.getLocalHost().getHostName(); } catch (Exception e){ h="unknown-host"; }
//    this.host = h;
//  }
//
//  private java.util.Map<String,Object> base() {
//    var m = new java.util.HashMap<String,Object>();
//    m.put("ts", java.time.Instant.now().toString());
//    m.put("service", props.getService());
//    m.put("env", props.getEnv());
//    m.put("host", host);
//    return m;
//  }
//
//  private String maskJson(java.util.Map<String,Object> data) {
//    try { return masking.mask(om.writeValueAsString(data)); }
//    catch (Exception e) { return "{\"message\":\"json_error\"}"; }
//  }
//
//  public void log(java.util.Map<String,Object> data){ logger.info(maskJson(data)); }
//
//  public void http(java.util.Map<String,Object> http, String level, String traceId, String spanId) {
//    var m = base();
//    m.put("level", level);
//    m.put("event","http_request");
//    m.put("trace_id", traceId);
//    m.put("span_id", spanId);
//    m.put("http", http);
//    log(m);
//  }
//
//  public void bizEvent(java.util.Map<String,Object> biz) {
//    var m = base();
//    m.put("level", "INFO");
//    m.put("event","biz_event");
//    m.putAll(biz);
//    log(m);
//  }
//
//  public void aiCall(java.util.Map<String,Object> ai) {
//    var m = base();
//    m.put("level", "INFO");
//    m.put("event","ai_call");
//    m.put("ai", ai);
//    log(m);
//  }
//}

package com.example.steady.logging.json;

import com.example.steady.logging.SteadyLoggingProperties;
import com.example.steady.logging.mask.MaskingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC; // <-- thêm dòng này

public class SafeLogger {
  private final Logger logger = LoggerFactory.getLogger("steady");
  private final MaskingUtil masking;
  private final SteadyLoggingProperties props;
  private final ObjectMapper om = new ObjectMapper();
  private final String host;

  public SafeLogger(MaskingUtil masking, SteadyLoggingProperties props) {
    this.masking = masking; this.props = props;
    String h; try { h = java.net.InetAddress.getLocalHost().getHostName(); } catch (Exception e){ h="unknown-host"; }
    this.host = h;
  }

  private java.util.Map<String,Object> base() {
    var m = new java.util.HashMap<String,Object>();
    m.put("ts", java.time.Instant.now().toString());
    m.put("service", props.getService());
    m.put("env", props.getEnv());
    m.put("host", host);

    // >>> đưa MDC vào JSON theo schema "mdc": { ... }
    var ctx = MDC.getCopyOfContextMap();
    if (ctx != null && !ctx.isEmpty()) {
      m.put("mdc", ctx);
    }
    return m;
  }

  private String maskJson(java.util.Map<String,Object> data) {
    try { return masking.mask(om.writeValueAsString(data)); }
    catch (Exception e) { return "{\"message\":\"json_error\"}"; }
  }

  public void log(java.util.Map<String,Object> data){ logger.info(maskJson(data)); }

  public void http(java.util.Map<String,Object> http, String level, String traceId, String spanId) {
    var m = base();
    m.put("level", level);
    m.put("event","http_request");
    m.put("trace_id", traceId);
    m.put("span_id", spanId);
    m.put("http", http);
    log(m);
  }

  public void bizEvent(java.util.Map<String,Object> biz) {
    var m = base();
    m.put("level", "INFO");
    m.put("event","biz_event");
    m.putAll(biz);
    log(m);
  }

  public void aiCall(java.util.Map<String,Object> ai) {
    var m = base();
    m.put("level", "INFO");
    m.put("event","ai_call");
    m.put("ai", ai);
    log(m);
  }
}

