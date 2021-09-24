package com.saamba.api.config.clients;

import com.neovisionaries.i18n.CountryCode;
import com.saamba.api.config.ClientConfig;
import com.saamba.api.dao.music.Song;
import com.saamba.api.enums.ClientTypes;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
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

    @Value("${client.spotify.timeout}")
    private int timeout;

    private final CountryCode countryCode = CountryCode.US;

    private SpotifyApi spotifyClient;

    public SpotifyClient() { }

    @Override
    @Scheduled(cron = "0/5?") // Every 5 minutes
    public void refreshCredentials() {
        log.info("Spotify credentials renewed.");
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
            log.error("Failed to create Spotify access token from supplied credentials.", e);
        }
        spotifyClient.setAccessToken(accessToken);
        return this;
    }

    public synchronized String[] getGenres() {
        String[] genres = {};
        try {
            GetAvailableGenreSeedsRequest request = spotifyClient
                    .getAvailableGenreSeeds()
                    .build();
            genres = request.execute();
            wait(timeout);
        } catch (IOException | SpotifyWebApiException | ParseException | InterruptedException e) {
            log.error("Failed to retrieve genres from Spotify.", e);
        }
        return genres;
    }

    public synchronized List<Song> getSongs(String genre) {
        List<Song> songs = new ArrayList<>();
        try {
            // Get most popular songs in the genre
            Recommendations recommendations = spotifyClient.getRecommendations()
                    .seed_genres(genre)
                    .limit(recLimit)
                    .max_popularity(popMax)
                    .min_popularity(popMin)
                    .build()
                    .execute();
            // Timeout necessary not to exceed Spotify's limits
            wait(timeout);
            // For each song get the artists
            for(TrackSimplified ts : recommendations.getTracks())
                // Seed each artist to get most popular songs
                for(ArtistSimplified a : ts.getArtists()) {
                    Track[] artists = spotifyClient
                            .getArtistsTopTracks(a.getId(), countryCode)
                            .build()
                            .execute();
                    wait(timeout);
                    // Parse each song
                    for (Track t : artists) {
                        songs.add(new Song(t, spotifyClient
                                .getAudioFeaturesForTrack(t.getId())
                                .build()
                                .execute()));
                        wait(timeout);
                    }
                }
        } catch (IOException | SpotifyWebApiException | ParseException | InterruptedException e) {
            log.error("Failed to retrieve songs from Spotify.", e);
        }
        return songs;
    }
}
