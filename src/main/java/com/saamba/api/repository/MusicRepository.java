package com.saamba.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.config.clients.SpotifyClient;

import com.saamba.api.dao.music.Genre;
import com.saamba.api.dao.music.Song;
import com.saamba.api.utils.ThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Repository("music")
@Slf4j
public class MusicRepository {

    @Value("${utils.task.max}")
    private int taskMax;

    @Value("${client.spotify.thread.limit}")
    private int threadMax;

    @Resource(name="spotify")
    SpotifyClient spotify;

    @Resource(name="genius")
    GeniusClient genius;

    public String updateMusic() {
        ThreadPool threadPool = new ThreadPool(taskMax, threadMax);
        log.info("Instantiating thread pool with " + threadMax +
                " threads and " + taskMax + " tasks.");
        String[] genres = spotify.getGenres();
        log.info(genres.length + " genres received from Spotify.");
        createDir();
        for (String g : genres)
            try {
                threadPool.execute(() -> genreToJSON(makeGenre(g)));
            } catch(Exception e) {
                log.error("Thread execution exceptions ", e);
            }
        threadPool.waitForCompletion();
        threadPool.stop();
        log.info("Thread pool terminated.");
        return "music updates completed";
    }

    private Genre makeGenre(String g) {
        Genre genre = new Genre(g);
        log.info("Creating genre " + g + ".");
        genre.setSongs(spotify.getSongs(g));
        log.info("Genre " + g + " has " + genre.getSongs().size() + " songs.");
        for(Song s : genre.getSongs())
            s.setLyrics(genius.getLyrics(s.getTitle(), s.getArtists()));
        log.info("Genre " + g + " has finished processing.");
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
            log.error("Genre " + g.getGenre() + " has failed to parse into json.", e);
        }
    }
}
