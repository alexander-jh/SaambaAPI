package com.saamba.api.config;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AppConfigTest {

    @Autowired
    private SpotifyClient spotifyClient;

    @Autowired
    private GeniusClient geniusClient;

    @Test
    void spotifyClientLoads() throws Exception {
        assertThat(spotifyClient).isNotNull();
    }

    @Test
    void geniusClientLoads() throws Exception {
        assertThat(geniusClient).isNotNull();
    }
}
