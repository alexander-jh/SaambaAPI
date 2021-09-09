package com.saamba.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.SpotifyClient;

import com.saamba.api.dao.Artist;
import com.saamba.api.dao.Genre;
import com.saamba.api.utils.ThreadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;

@Repository
public class MusicRepository {

    @Resource(name="spotify")
    SpotifyClient spotify;

    @Autowired
    ThreadPool threadPool;

    public String updateMusic() {
        String[] genres = spotify.getGenres();
        for (String g : genres)
            try {
                threadPool.execute(() -> {
                    genreToJSON(makeGenre(g));
                });
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

        return "music updates completed";
    }

    private Genre makeGenre(String g) {
        Genre genre = new Genre(g);
        genre.setArtists(spotify.getArtists(genre));
        for(Artist a : genre.getArtists())
            a.setSongs(spotify.getSongs(a));
        return genre;
    }

    private void genreToJSON(Genre g) {
        ObjectMapper mapper = new ObjectMapper();
        String fileName = "json/" + g.getGenre() + ".json";
        try {
            mapper.writeValue(new File(fileName), g);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
