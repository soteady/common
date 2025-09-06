package com.example.steady.logging;

import com.example.steady.logging.SteadyLoggingProperties;

@org.springframework.boot.autoconfigure.AutoConfiguration
@org.springframework.boot.context.properties.EnableConfigurationProperties(SteadyLoggingProperties.class)
@org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication(type = org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET)
public class SteadyLoggingAutoConfiguration {
    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.mask.MaskingUtil maskingUtil(SteadyLoggingProperties props) {
        return new com.example.steady.logging.mask.MaskingUtil(props.getMasking().getPatterns(), props.getMasking().getMaskWith());
    }

    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.json.SafeLogger safeLogger(com.example.steady.logging.mask.MaskingUtil maskingUtil, SteadyLoggingProperties props) {
        return new com.example.steady.logging.json.SafeLogger(maskingUtil, props);
    }

    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.error.ErrorCodeRegistry errorCodeRegistry() {
        return new com.example.steady.logging.error.ErrorCodeRegistry("error-codes.yaml");
    }

    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.error.ErrorAdvice errorAdvice(com.example.steady.logging.error.ErrorCodeRegistry registry, com.example.steady.logging.json.SafeLogger safeLogger) {
        return new com.example.steady.logging.error.ErrorAdvice(registry, safeLogger);
    }

    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.web.HttpLoggingFilter httpLoggingFilter(com.example.steady.logging.json.SafeLogger safeLogger) {
        return new com.example.steady.logging.web.HttpLoggingFilter(safeLogger);
    }

    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.ai.AiGuardService aiGuardService(SteadyLoggingProperties props) {
        return new com.example.steady.logging.ai.AiGuardService(props);
    }

    @org.springframework.context.annotation.Bean
    public com.example.steady.logging.ai.AiLoggingSupport aiLoggingSupport(com.example.steady.logging.json.SafeLogger log, SteadyLoggingProperties props) {
        return new com.example.steady.logging.ai.AiLoggingSupport(log, props);
    }
}
