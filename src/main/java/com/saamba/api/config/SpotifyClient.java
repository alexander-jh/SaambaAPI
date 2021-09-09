package com.saamba.api.config;

import com.saamba.api.dao.Artist;
import com.saamba.api.dao.Genre;
import com.saamba.api.dao.Song;
import com.saamba.api.enums.ClientTypes;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.apache.hc.core5.http.ParseException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private SpotifyApi spotifyClient;

    public SpotifyClient() {

    }

    @Override
    public void refreshCredentials() {
        spotifyClient.setRefreshToken(spotifyClient.getRefreshToken());
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.Spotify; }

    @Autowired
    public SpotifyClient(@Value("${client.spotify.secretkey}") String secretKey,
                         @Value("${client.spotify.accesskey}") String accessKey) {
        spotifyClient = new SpotifyApi.Builder()
                .setClientId(accessKey)
                .setClientSecret(secretKey)
                .build();
        String accessToken = "";
        try {
            ClientCredentialsRequest cr = spotifyClient.clientCredentials()
                    .build();
            ClientCredentials cred = cr.execute();
            accessToken = cred.getAccessToken();
        } catch(IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        spotifyClient.setAccessToken(accessToken);
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

    public Artist[] getArtists(Genre genre) {
        Artist[] artists = {};
        try {
            Recommendations recommendations = spotifyClient.getRecommendations()
                    .seed_genres(genre.getGenre())
                    .limit(recLimit)
                    .max_popularity(popMax)
                    .min_popularity(popMin)
                    .build()
                    .execute();
            Set<ArtistSimplified> uniqueArtists = new HashSet<>();

            for(TrackSimplified t : recommendations.getTracks())
                Arrays.stream(t.getArtists()).iterator().forEachRemaining(
                        uniqueArtists::add
                );

            artists = new Artist[uniqueArtists.size()];

            Iterator<ArtistSimplified> it = uniqueArtists.iterator();
            for(int i = 0; !uniqueArtists.isEmpty(); ++i) {
                ArtistSimplified a = it.next();
                artists[i] = new Artist(a);
                uniqueArtists.remove(a);
            }

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return artists;
    }

    public Song[] getSongs(Artist artist) {
        Song[] songs = {};
        try {
            Recommendations recommendations = spotifyClient.getRecommendations()
                    .seed_artists(artist.getId())
                    .limit(recLimit)
                    .max_popularity(popMax)
                    .min_popularity(popMin)
                    .build()
                    .execute();

            songs = new Song[recommendations.getTracks().length];

            for(int i = 0; i < songs.length; ++i)
                songs[i] = new Song(recommendations.getTracks()[i]);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return songs;
    }
}
