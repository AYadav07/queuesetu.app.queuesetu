package com.queuesetu.queuesetu.config;

import com.queuesetu.boot.core.restclient.config.RestClientConfig;
import com.queuesetu.boot.core.restclient.factory.RestClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

@Configuration
@Import(RestClientConfig.class)
public class BffConfig {

    @Bean
    public RestClientFactory restClientFactory(RestClient.Builder restClientBuilder) {
        return new RestClientFactory(restClientBuilder);
    }
}
