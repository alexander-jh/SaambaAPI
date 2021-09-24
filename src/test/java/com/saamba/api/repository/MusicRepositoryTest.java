package com.saamba.api.repository;

import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.config.clients.SpotifyClient;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MusicRepositoryTest {

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
