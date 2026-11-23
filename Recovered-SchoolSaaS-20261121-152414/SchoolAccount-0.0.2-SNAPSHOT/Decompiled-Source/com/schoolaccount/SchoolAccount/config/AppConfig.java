/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.Module
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
 *  com.schoolaccount.SchoolAccount.config.AppConfig
 *  com.schoolaccount.SchoolAccount.config.PaymentConfig
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.client.RestTemplate
 */
package com.schoolaccount.SchoolAccount.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.schoolaccount.SchoolAccount.config.PaymentConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Value(value="${payment.gateway.api.key:dummy-api-key}")
    private String paymentGatewayApiKey;
    @Value(value="${payment.gateway.secret:dummy-secret}")
    private String paymentGatewaySecret;
    @Value(value="${payment.gateway.url:https://api.example-payment-gateway.com}")
    private String paymentGatewayUrl;
    @Value(value="${superadmin.account.id:super-admin-account}")
    private String superAdminAccountId;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule((Module)new JavaTimeModule());
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PaymentConfig paymentConfig() {
        PaymentConfig config = new PaymentConfig();
        return config;
    }
}

