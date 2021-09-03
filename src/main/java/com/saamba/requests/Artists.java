package com.saamba.requests;

import com.saamba.helpers.Constants;
import com.saamba.wrappers.SpotifyClient;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;

import java.util.HashMap;
import java.util.Map;

public class Artists {

    private Map<String, String> artists;

    public Artists(String c) {
        artists = new HashMap<>();

        try {
            Recommendations recommendations = SpotifyClient.getClient()
                    .getRecommendations()
                    .limit(Constants.RECOMMENDATION_LIMIT)
                    .max_popularity(Constants.POPULARITY_LIMIT)
                    .seed_genres(c)
                    .build()
                    .execute();
            parseArtists(recommendations);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Map<String, String> getArtists() {
        return artists;
    }

    private void parseArtists(Recommendations r) {
        for(TrackSimplified t : r.getTracks()) {
            ArtistSimplified[] trackArtists = t.getArtists();
            for(ArtistSimplified a : trackArtists)
                artists.put(a.getName(), a.getId());
        }
    }
}
