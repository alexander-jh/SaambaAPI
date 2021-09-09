package com.saamba.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean("spotify")
    public ClientConfig spotifyAPI() { return new SpotifyClient(); }

    @Bean("ibm")
    public ClientConfig ibmAPI() { return new IBMClient(); }

    @Bean("aws")
    public ClientConfig awsAPI() {return new AWSConfig(); }

}
