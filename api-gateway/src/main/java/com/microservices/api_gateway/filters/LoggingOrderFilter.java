package com.microservices.api_gateway.filters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j // add this filter in application.yml file
public class LoggingOrderFilter extends AbstractGatewayFilterFactory<LoggingOrderFilter.Config> {

    // No need for constructor, Spring will handle the injection automatically
    public LoggingOrderFilter() {
        super(Config.class);  // Spring will automatically inject Config.class
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Order Filter pre : {}", exchange.getRequest().getURI());
            return chain.filter(exchange);
        };
    }

    // The Config class is used as a placeholder for the filter configuration
    public static class Config {}
}
