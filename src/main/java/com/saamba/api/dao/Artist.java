package com.saamba.api.dao;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Artist {

    private final String id;
    private final String name;
    private final String uri;
    private List<Song> songs;

    public Artist(ArtistSimplified artist) {
        this.id = artist.getId();
        this.name = artist.getName();
        this.uri = artist.getUri();
        this.songs = new ArrayList<>();
    }

    public String getId() { return this.id; }

    public String getName() { return this.name; }

    public String getURI() { return this.uri; }

    public Song[] getSongs() { return this.songs.toArray(new Song[0]); }

    public void setSongs(Song[] s) { this.songs = Arrays.asList(s); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Artist{id=").append(this.id)
          .append(", name=").append(this.name)
          .append(", uri=").append(this.uri)
          .append(", songs=[");
        for(Song s : this.songs)
            sb.append(s.toString()).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");
        return sb.toString();
    }
}
