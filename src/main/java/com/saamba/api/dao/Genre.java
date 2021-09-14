package com.saamba.api.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Genre {
    private final String genre;
    private List<Artist> artists;

    public Genre(String genre) {
        this.genre = genre;
        this.artists = new ArrayList<>();
    }

    public String getGenre() { return this.genre; }

    public List<Artist> getArtists() { return this.artists; }

    public void setArtists(Artist[] artists) {
        for(Artist a : artists)
            if(a.getName().length() > 0) this.artists.add(a);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genre{ genre=").append(this.genre)
                .append(", artists=[");
        for(Artist a : this.artists)
            sb.append(a.toString()).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");
        return sb.toString();
    }
}
