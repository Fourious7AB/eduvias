/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.config.TomcatConfig
 *  org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
 *  org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
 *  org.springframework.boot.web.server.WebServerFactoryCustomizer
 *  org.springframework.context.annotation.Configuration
 */
package com.neel.Teacher.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig
implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer[]{connector -> {
            connector.setMaxParameterCount(20);
            connector.setMaxPostSize(2048);
        }});
    }
}

