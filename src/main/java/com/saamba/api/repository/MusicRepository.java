package com.saamba.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.config.clients.SpotifyClient;

import com.saamba.api.dao.music.Genre;
import com.saamba.api.dao.music.Song;
import com.saamba.api.utils.ThreadPool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.File;
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

    @Resource(name="genius")
    GeniusClient genius;

    public String updateMusic() {
        ThreadPool threadPool = new ThreadPool(taskMax, threadMax);
        String[] genres = spotify.getGenres();
        createDir();
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
        genre.setSongs(spotify.getSongs(g));
        for(Song s : genre.getSongs())
            s.setLyrics(genius.getLyrics(s.getTitle(), s.getArtists()));
        return genre;
    }

    private void createDir() {
        File dir = new File("json");
        if(!dir.exists())
            dir.mkdirs();
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
