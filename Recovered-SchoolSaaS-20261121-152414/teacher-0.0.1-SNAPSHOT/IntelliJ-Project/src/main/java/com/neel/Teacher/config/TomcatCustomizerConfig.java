/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.config.TomcatCustomizerConfig
 *  org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.neel.Teacher.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatCustomizerConfig {
    @Bean
    public TomcatConnectorCustomizer connectorCustomizer() {
        return connector -> {
            connector.setMaxParameterCount(100);
            connector.setMaxPostSize(2048);
        };
    }
}

