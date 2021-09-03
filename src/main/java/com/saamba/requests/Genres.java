package com.saamba.requests;

import com.saamba.wrappers.SpotifyClient;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Genres {

    private final String[] genres;

    public Genres() {
        String[] g = {};
        try {
            GetAvailableGenreSeedsRequest genreRequest = SpotifyClient.getClient()
                    .getAvailableGenreSeeds()
                    .build();
            g = genreRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        genres = g;
    }

    public String[] getGenres() {
        return genres;
    }
}
