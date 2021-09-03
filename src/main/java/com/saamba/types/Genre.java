package com.saamba.types;

import com.saamba.requests.Artists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Genre {
    private final String genre;
    private List<Artist> artists;

    public Genre(String genre) {
        artists = new ArrayList<>();
        this.genre = genre;
        getArtists();
    }

    private void getArtists() {
        Map<String, String> artistResults = (new Artists(this.genre)).getArtists();
        artistResults.forEach((String a, String id) -> {
            this.artists.add(new Artist(a, id));
        });
    }
}
