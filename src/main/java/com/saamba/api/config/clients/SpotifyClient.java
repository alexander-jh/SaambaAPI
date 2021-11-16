package com.saamba.api.config.clients;

import com.neovisionaries.i18n.CountryCode;
import com.saamba.api.config.ClientConfig;
import com.saamba.api.config.CredentialManager;
import com.saamba.api.dao.music.Song;
import com.saamba.api.enums.ClientTypes;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;

import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * This client is a bit different in that Spotify throttles API
 * calls based on the number received in a 30 second window. The
 * workaround for this is consistent timeouts between consecutive
 * calls.
 */
@Service
@Slf4j
@EnableScheduling
public class SpotifyClient implements ClientConfig {

    @Autowired
    private CredentialManager creds;

    @Value("${client.spotify.secretkey}")
    private String secretKey;

    @Value("${client.spotify.accesskey}")
    private String accessKey;

    private final int refreshRate = 300000;

    @Value("${client.spotify.recommendation.limit}")
    private int recLimit;

    @Value("${client.spotify.popularity.limit}")
    private int popMax;

    @Value("${client.spotify.popularity.min}")
    private int popMin;

    @Value("${client.spotify.timeout}")
    private int timeout;

    @Value("${client.spotify.song.max}")
    private int songsPerGenre;

    @Value("${client.spotify.artist.max}")
    private int songsPerArtist;

    @Resource(name = "genius")
    GeniusClient geniusClient;

    private final CountryCode countryCode = CountryCode.US;

    protected SpotifyApi spotifyClient;

    public SpotifyClient() { }

    /**
     * Cron expression to refresh credentials every 5 minutes.
     */
    @Override
    @Scheduled(fixedRate = refreshRate) // Every 5 minutes
    public synchronized void refreshCredentials() {
        this.init();
        log.info("Spotify credentials renewed.");
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.Spotify; }

    /**
     * Necessary function which instantiates after the no-arg constructor
     * is invoked. Needed since values aren't injected until after creation
     * of the class.
     * @return      - spotify agent
     */
    @PostConstruct
    public SpotifyClient init() {
        this.secretKey = this.creds.getSecretValue(this.secretKey);
        this.accessKey = this.creds.getSecretValue(this.accessKey);
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

    /**
     * Gets a list of all available genres on Spotify.
     * @return      - string array of genre names
     */
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

    /**
     * Returns all the songs for a given genre as a list. Internal
     * logic and comments describe steps.
     * @param genre     - string genre name
     * @return          - list of songs in a genre
     */
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
            for(TrackSimplified ts : recommendations.getTracks()) {
                // Seed each artist to get most popular songs
                for (ArtistSimplified a : ts.getArtists()) {
                    Track[] artists = spotifyClient
                            .getArtistsTopTracks(a.getId(), countryCode)
                            .build()
                            .execute();
                    wait(timeout);
                    // Parse each song
                    for (Track t : artists) {
                        AudioFeatures af = spotifyClient
                                .getAudioFeaturesForTrack(t.getId())
                                .build()
                                .execute();
                        if (af != null) {
                            Song s = new Song(t, af);
                            s.setLyrics(geniusClient.getLyrics(s.getTitle(), s.getArtists()));
                            songs.add(s);
                            // Breakpoint if thresholds are passed
                            if(songs.size() > songsPerArtist)
                                break;
                            if(songs.size() > songsPerGenre)
                                return songs;
                        }
                        wait(timeout);
                    }
                }
            }
        } catch (IOException | SpotifyWebApiException | ParseException | InterruptedException e) {
            log.error("Failed to retrieve songs from Spotify.", e);
        }
        return songs;
    }

    public synchronized String searchSong(String song){
        log.info("Query: " + song);
        SearchItemRequest searchItemRequest = spotifyClient
                .searchItem(song, ModelObjectType.TRACK.getType())
                .build();
        String uri = "";
        try {
            uri =  searchItemRequest.execute()
                    .getTracks()
                    .getItems()[0]
                    .getUri();
        } catch (IOException | SpotifyWebApiException | ArrayIndexOutOfBoundsException | ParseException e) {;
            log.error("Error: " + e.getMessage());
        }
        return uri;
    }

    /**
     * Test function for verifying credential refresh.
     * @return      - string of access token
     */
    protected String getCurrentAccessToken() {
        return spotifyClient.getAccessToken();
    }
}
