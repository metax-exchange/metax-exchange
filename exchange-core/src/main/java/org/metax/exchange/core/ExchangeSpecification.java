package org.metax.exchange.core;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@ToString
public class ExchangeSpecification {

    private Account account;

    private ApiSecret apiSecret;

    private Map parameterMap;

    @Builder.Default
    private ResilienceSpecification resilienceSpecification = new ResilienceSpecification();

    @Data
    @AllArgsConstructor
    public static class Account {
        private String userName;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class ApiSecret {
        private String apiKey;
        private String secretKey;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResilienceSpecification {
        private boolean enabledRetry = false;
        private boolean enabledRateLimiter = false;
    }

}
