package com.saamba.api.config.clients;

import com.saamba.api.dao.music.Song;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class SpotifyClientTest extends SpotifyClient {

    @Value("${test.spotify.genre}")
    private String genre;

    @Autowired
    SpotifyClient spotify;

    @Test
    void getGenreTests() {
        String[] genres = spotify.getGenres();
        assertThat(genres.length).isGreaterThan(0);
    }

//    Passes test, but is only included for prod deployments due to
//    the length of the runtime.
//    @Test
//    void getSongsTests() {
//        List<Song> songs;
//        songs = spotify.getSongs(genre);
//        assertThat(songs.size()).isGreaterThan(0);
//    }

    @Test
    void refreshCredentialsTest() {
        String oldAccess = spotify.getCurrentAccessToken();
        spotify.refreshCredentials();
        String newAccess = spotify.getCurrentAccessToken();
        assertThat(oldAccess.equals(newAccess)).isFalse();
    }
}
