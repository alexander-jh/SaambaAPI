package com.saamba.api.dao;

import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;

public class Song {

    private final String id;
    private final String title;
    private final String uri;
    private String lyrics;
    private List<String> artists;

    public Song(TrackSimplified track) {
        this.artists = new ArrayList<>();
        this.id = track.getId();
        this.title = track.getName();
        this.uri = track.getUri();
        for(ArtistSimplified a : track.getArtists())
            this.artists.add(a.getName());
        this.lyrics = "";
    }

    public List<String> getArtists() { return this.artists; }

    public String getId() { return this.id; }

    public String getTitle() { return this.title; }

    public String getURI() { return this.uri; }

    public String getLyrics() { return this.lyrics; }

    public void setLyrics(String lyrics) { this.lyrics = lyrics; }

    @Override
    public String toString() {
        return (new StringBuilder())
                .append("Song{ id=").append(this.id)
                .append(", title=").append(this.title)
                .append(", uri=").append(this.uri)
                .append(", lyrics=\"").append(this.lyrics)
                .append("\"}").toString();
    }
}
