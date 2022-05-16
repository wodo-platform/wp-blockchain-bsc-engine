package io.wodo.config;


import io.wodo.logging.LoggingProperties;
import io.wodo.logging.server.ServerLoggingFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebConfig {

    @Bean
    public WebFilter loggingFilter() {
        LoggingProperties properties = LoggingProperties.builder()
                .logRequestId(true)
                .requestIdPrefix("REQ-ID")
                .logHeaders(true)
                .logCookies(true)
                .logBody(true)
                .build();

        return ServerLoggingFilterFactory.defaultFilter(properties, properties);
    }
}
