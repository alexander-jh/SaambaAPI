package com.saamba.api.config;

import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.config.clients.IBMClient;
import com.saamba.api.config.clients.SpotifyClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfig {

    @Bean("spotify")
    public SpotifyClient spotifyAPI() {
        log.info("SpotifyClient bean created.");
        return new SpotifyClient();
    }

    @Bean("ibm")
    public IBMClient ibmAPI() {
        log.info("IBMClient bean created.");
        return new IBMClient();
    }

    @Bean("aws")
    public AWSConfig awsAPI() {
        log.info("AWSConfig bean created.");
        return new AWSConfig();
    }

    @Bean("genius")
    public GeniusClient geniusAPI() {
        log.info("GeniusClient bean created.");
        return new GeniusClient();
    }
}
