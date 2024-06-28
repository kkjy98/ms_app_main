package com.kelvin.ms_app.config;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Timeout;
import org.apache.http.HeaderElementIterator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public ClientHttpRequestInterceptor apiLogInterceptor() {
        return new ApiLogInterceptor();
    }
    @Bean
    public ClientHttpRequestInterceptor requestResponseLoggingInterceptor() {
        return new RequestResponseLoggingInterceptor();
    }

    @Bean
    @Qualifier("apiLogInterceptor")
    public RestTemplate apirestTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(clientHttpRequestFactory());
        RestTemplate apirestTemplate = new RestTemplate(factory);
        apirestTemplate.setInterceptors(Collections.singletonList(apiLogInterceptor()));
        return apirestTemplate;
    }

    @Bean
    @Qualifier("requestResponseLoggingInterceptor")
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(clientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(requestResponseLoggingInterceptor()));
        return restTemplate;
    }
    @Bean
    @Qualifier("keycloakRestTemplate")
    public RestTemplate keycloakRestTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(clientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(requestResponseLoggingInterceptor()));
        return restTemplate;
    }
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient());
        return clientHttpRequestFactory;
    }
    @Bean
    public HttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofDays(90000))
                .setConnectTimeout(Timeout.ofDays(90000))
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .build();
    }
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(100);
        return poolingConnectionManager;
    }

}
