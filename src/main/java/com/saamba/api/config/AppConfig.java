package com.saamba.api.config;

import com.saamba.api.config.clients.DiscoveryClient;
import com.saamba.api.config.clients.ToneClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration reference for each of the different clients
 * and configs used at the service layer.
 *
 * IBM services are identified by unique clients and are not abstracted
 * due to lack of homogenized API call and credentials.
 */
@Configuration
@Slf4j
public class AppConfig {

    @Bean("discovery")
    public DiscoveryClient discoveryAPI() {
        log.info("DiscoveryClient bean created.");
        return new DiscoveryClient();
    }

    @Bean("tone")
    public ToneClient toneAPI() {
        log.info("ToneClient bean created.");
        return new ToneClient();
    }

    @Bean("aws")
    public AWSConfig awsAPI() {
        log.info("AWSConfig bean created.");
        return new AWSConfig();
    }
}
