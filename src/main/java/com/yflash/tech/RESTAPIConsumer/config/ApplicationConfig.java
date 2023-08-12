package com.yflash.tech.RESTAPIConsumer.config;

import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    private final Environment environment;

    @Autowired
    public ApplicationConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {

        String connection = environment.getProperty("restTemplate.connectionTimeout");
        String readTimeoutStr = environment.getProperty("restTemplate.readTimeout");

        int connectionTimeout = StringUtils.isNotEmpty(connection) ? Integer.parseInt(connection) : 0;
        int readTimeout = StringUtils.isNotEmpty(connection) ? Integer.parseInt(readTimeoutStr) : 0;

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);

        return clientHttpRequestFactory;
    }

}
