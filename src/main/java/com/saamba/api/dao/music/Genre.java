package com.saamba.api.dao.music;

import java.util.ArrayList;
import java.util.List;

public class Genre {
    private final String genre;
    private List<Song> songs;

    public Genre(String genre) {
        this.genre = genre;
        this.songs = new ArrayList<>();
    }

    public String getGenre() { return this.genre; }

    public List<Song> getSongs() { return this.songs; }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genre{ genre=").append(this.genre)
                .append(", artists=[");
        for(Song s : this.songs)
            sb.append(s.toString()).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");
        return sb.toString();
    }
}
