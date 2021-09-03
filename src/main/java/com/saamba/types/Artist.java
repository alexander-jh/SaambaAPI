package com.saamba.types;

import com.saamba.requests.Songs;

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

    private void getMostPopularSongs(String id) {
        Set<Song> songResults = (new Songs(this.name, id)).getSongs();
        this.songs.addAll(songResults);
    }
}
