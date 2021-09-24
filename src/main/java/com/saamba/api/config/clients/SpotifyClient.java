package com.saamba.api.config.clients;

import com.saamba.api.config.ClientConfig;
import com.saamba.api.dao.music.Song;
import com.saamba.api.enums.ClientTypes;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class SpotifyClient implements ClientConfig {

    @Value("${client.spotify.secretkey}")
    private String secretKey;

    @Value("${client.spotify.accesskey}")
    private String accessKey;

    @Value("${client.spotify.recommendation.limit}")
    private int recLimit;

    @Value("${client.spotify.popularity.limit}")
    private int popMax;

    @Value("${client.spotify.popularity.min}")
    private int popMin;

    private SpotifyApi spotifyClient;

    public SpotifyClient() { }

    @Override
    @Scheduled(cron = "0/5?") // Every 5 minutes
    public void refreshCredentials() {
        spotifyClient.setRefreshToken(spotifyClient.getRefreshToken());
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.Spotify; }

    @PostConstruct
    public SpotifyClient init() {
        spotifyClient = new SpotifyApi.Builder()
                .setClientId(accessKey)
                .setClientSecret(secretKey)
                .build();
        String accessToken = "";
        try {
            accessToken = spotifyClient.clientCredentials()
                    .build()
                    .execute()
                    .getAccessToken();
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        spotifyClient.setAccessToken(accessToken);
        return this;
    }

    public String[] getGenres() {
        String[] genres = {};
        try {
            GetAvailableGenreSeedsRequest request = spotifyClient
                    .getAvailableGenreSeeds()
                    .build();
            genres = request.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return genres;
    }

    public List<Song> getSongs(String genre) {
        List<Song> songs = new ArrayList<>();
        try {
            Recommendations recommendations = spotifyClient.getRecommendations()
                    .seed_genres(genre)
                    .limit(recLimit)
                    .max_popularity(popMax)
                    .min_popularity(popMin)
                    .build()
                    .execute();
            for(TrackSimplified t : recommendations.getTracks()) {
                songs.add(new Song(t, spotifyClient
                        .getAudioFeaturesForTrack(t.getId())
                        .build()
                        .execute()));
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return songs;
    }
}
