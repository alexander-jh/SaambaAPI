package com.saamba.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.config.clients.SpotifyClient;

import com.saamba.api.dao.music.Genre;
import com.saamba.api.dao.music.Song;
import com.saamba.api.entity.music.MusicService;
import com.saamba.api.utils.ThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controls logic for pulling song data from Spotify and lyrics
 * from Genius. Pulls top songs by genre, then uses associated
 * artists to query songs for each artist.
 *
 * Uses DynamoDB to record unique songs. If a song doesn't exist
 * it adds the new song. If it exists checks if lyrics exist if
 * not updates them.
 */
@Repository("music")
@Slf4j
public class MusicRepository {

    @Value("${utils.task.max}")
    private int taskMax;

    @Value("${client.spotify.thread.limit}")
    private int threadMax;

    @Autowired
    private MusicService musicService;

    @Resource(name="spotify")
    SpotifyClient spotify;

    @Resource(name="genius")
    GeniusClient genius;

    /**
     * Entry point for service. uses a thread pool to get song data.
     * Each thread is given a specific genre.
     * @return      - acknowledgement of completion
     */
    public String updateMusic() {
        ThreadPool threadPool = new ThreadPool(taskMax, threadMax);
        log.info("Instantiating thread pool with " + threadMax +
                " threads and " + taskMax + " tasks.");
        String[] genres = spotify.getGenres();
        log.info(genres.length + " genres received from Spotify.");
        createDir();
        for (String g : genres)
            try {
                threadPool.execute(() -> makeGenre(g));
            } catch(Exception e) {
                log.error("Thread execution exceptions ", e);
            }
        threadPool.waitForCompletion();
        threadPool.stop();
        log.info("Thread pool terminated.");
        return "music updates completed";
    }

    /**
     * Uses music service bean to update the database with the newly
     * pulled song data.
     * @param g     - string of genre title
     */
    private void makeGenre(String g) {
        log.info("Creating genre " + g + ".");
        Genre genre = new Genre(g);
        List<Song> songs = spotify.getSongs(g);
        log.info("Genre " + g + " has " + songs.size() + " songs.");
        for(Song s : songs) {
            s.setLyrics(genius.getLyrics(s.getTitle(), s.getArtists()));
            if(!musicService.songExists(s.getURI(), g))
                musicService.createMusic(s, genre);
            else
                musicService.updateLyrics(s, genre);
        }
        backfillJSON(g);
        log.info("Genre " + g + " has finished processing.");
    }

    /**
     * Creates json directory for backfill if it doesn't exist.
     */
    private void createDir() {
        File dir = new File("json");
        if(!dir.exists())
            dir.mkdirs();
    }

    /**
     * Queries the database to pull all songs associated with a
     * genre and uses jackson object mapper to convert the JPA
     * entity into a JSON.
     * @param g     - string genre name
     */
    private void genreToJSON(Genre g) {
        ObjectMapper mapper = new ObjectMapper();
        String fileName = "json/" + g.getGenre() + ".json";
        try {
            mapper.writeValue(Paths.get(fileName).toFile(), g);
        } catch(IOException e) {
            log.error("Genre " + g.getGenre() + " has failed to parse into json.", e);
        }
    }

    /**
     * Invokes functionality to create JSON for a genre.
     * @param g     - string genre name
     */
    private void backfillJSON(String g) {
        log.info("Genre " + g + " starting backfill.");
        genreToJSON(musicService.exportGenre(g));
        log.info("Genre " + g + " successfully backfilled.");
    }
}
