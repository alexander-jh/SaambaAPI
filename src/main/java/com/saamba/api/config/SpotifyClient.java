package com.saamba.api.config;

import com.neovisionaries.i18n.CountryCode;
import com.saamba.api.dao.Genre;
import com.saamba.api.dao.Song;
import com.saamba.api.enums.ClientTypes;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.hc.core5.http.ParseException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
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

    private static final CountryCode countryCode = CountryCode.US;

    private SpotifyApi spotifyClient;

    public SpotifyClient() { }

    @Override
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
            for(TrackSimplified t : recommendations.getTracks())
                songs.add(new Song(t));
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return songs;
    }
}
