package com.saamba.api.config;

import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.config.clients.DiscoveryClient;
import com.saamba.api.config.clients.SpotifyClient;
import com.saamba.api.config.clients.ToneClient;
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

    @Bean("genius")
    public GeniusClient geniusAPI() {
        log.info("GeniusClient bean created.");
        return new GeniusClient();
    }

    @Bean("twitter")
    public TwitterConfig twitterApi() {
        log.info("TwitterConfig bean created.");
        return new TwitterConfig();
    }
}
