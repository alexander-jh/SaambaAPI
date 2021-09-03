package com.saamba.types;

import com.saamba.requests.Songs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Artist {
    private final String name;
    private List<Song> songs;

    public Artist(String name, String id) {
        this.name = name;
        this.songs = new ArrayList<>();
        getMostPopularSongs(id);
    }

    public String getName() { return this.name; }

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        JSONArray jSongs = new JSONArray();
        for(Song s : songs) {
            if(s.getTitle().length() > 0 && s.getLyrics().length() > 0)
                jSongs.add(s.toJSON());
        }
        if(!jSongs.isEmpty() && name.length() > 0) {
            json.put("Name", name);
            json.put("Songs", jSongs);
        }
        return json;
    }

    public int songCount() { return songs.size(); }

    private void getMostPopularSongs(String id) {
        Set<Song> songResults = (new Songs(this.name, id)).getSongs();
        this.songs.addAll(songResults);
    }
}
