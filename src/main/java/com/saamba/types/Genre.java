package com.saamba.types;

import com.saamba.requests.Artists;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Genre {
    private final String genre;
    private List<Artist> artists;

    public Genre(String genre) {
        artists = new ArrayList<>();
        this.genre = genre;
        parseArtists();
    }

    public List<Artist> getArtists() { return artists; }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONArray jArtists = new JSONArray();
        for(Artist a : artists)
            jArtists.add(a.getJSON());
        json.put("Artists", jArtists);
        return json;
    }

    private void parseArtists() {
        Map<String, String> artistResults = (new Artists(this.genre)).getArtists();
        artistResults.forEach((String a, String id) -> {
            this.artists.add(new Artist(a, id));
        });
    }
}
