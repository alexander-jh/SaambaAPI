package com.saamba.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean("spotify")
    public SpotifyClient spotifyAPI() { return new SpotifyClient(); }

    @Bean("ibm")
    public IBMClient ibmAPI() { return new IBMClient(); }

    @Bean("aws")
    public AWSConfig awsAPI() {return new AWSConfig(); }

}
