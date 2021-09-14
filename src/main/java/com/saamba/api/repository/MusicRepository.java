package com.saamba.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.MusixMatchClient;
import com.saamba.api.config.SpotifyClient;

import com.saamba.api.dao.Artist;
import com.saamba.api.dao.Genre;
import com.saamba.api.dao.Song;
import com.saamba.api.utils.ThreadPool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.IOException;
import java.nio.file.Paths;

@Repository("music")
public class MusicRepository {

    @Value("${utils.task.max}")
    private int taskMax;

    @Value("${utils.thread.max}")
    private int threadMax;

    @Resource(name="spotify")
    SpotifyClient spotify;

    @Resource(name="musix")
    MusixMatchClient musixMatch;

    public String updateMusic() {
        ThreadPool threadPool = new ThreadPool(taskMax, threadMax);
        String[] genres = spotify.getGenres();
        for (String g : genres)
            try {
                threadPool.execute(() -> genreToJSON(makeGenre(g)));
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        threadPool.waitForCompletion();
        threadPool.stop();
        return "music updates completed";
    }

    private Genre makeGenre(String g) {
        Genre genre = new Genre(g);
        genre.setArtists(spotify.getArtists(genre));
        for(Artist a : genre.getArtists()) {
            Song[] songs = spotify.getSongs(a);
            for(Song s : songs) {
                String lyrics = musixMatch.getLyrics(a.getName(), s.getTitle());
                if(lyrics.length() > 0) s.setLyrics(lyrics);
            }
            a.setSongs(songs);
        }
        return genre;
    }

    private void genreToJSON(Genre g) {
        ObjectMapper mapper = new ObjectMapper();
        String fileName = "json/" + g.getGenre() + ".json";
        try {
            mapper.writeValue(Paths.get(fileName).toFile(), g);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
