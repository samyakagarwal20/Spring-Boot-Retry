package com.yflash.tech.RESTAPIConsumer.service.impl;

import com.yflash.tech.RESTAPIConsumer.model.out.User;
import com.yflash.tech.RESTAPIConsumer.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Environment environment;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(Environment environment, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.environment = environment;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Override
    @Retryable(label = "retry-getAllUsers()", maxAttempts = 4, backoff = @Backoff(delay = 2000), retryFor = {IOException.class}, noRetryFor = {SQLException.class})
    public List<User> getAllUsers() {
        List<User> result = null;
        try{
            LOGGER.info("Preparing the request ...");
            String wsUrl = environment.getProperty("producer.api.url");
            LOGGER.info("\t|--- Setting up the headers");
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            LOGGER.info("\t|--- Setting up the request body");
            HttpEntity<String> reqEntity = new HttpEntity<>(headers);
            ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<List<User>>() {
            };

            LOGGER.info("Fetching users data ...");
            ResponseEntity<List<User>> response = restTemplate.exchange(wsUrl, HttpMethod.GET, reqEntity, responseType);

            if (response.getStatusCode().is2xxSuccessful()) {
                result = response.getBody();
                LOGGER.info("Data fetched successfully !");
            }
        } catch (Exception e) {
            LOGGER.error("Error in getAllUsers() by {} : {}", e.getClass().getCanonicalName(), e.getMessage());
        }
        return result;
    }

}
