package com.saamba.api.dao.music;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.saamba.api.config.clients.GeniusClient;
import com.saamba.api.entity.music.Music;
import com.saamba.api.enums.GenreConstants;
import com.saamba.api.utils.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts DAO POJO for a song and creates the entity object
 * for CRUD and DB related actions.
 */
@Slf4j
@Component
public class SongToMusic {

    @Value("${utils.task.max}")
    private int taskMax;

    @Value("${client.spotify.thread.limit}")
    private int threadMax;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private GeniusClient geniusClient;

    /**
     * Converts a song to the entity music representation and inserts
     * the song into the DB.
     * @param song  - song object
     * @param genre - genre object
     */
    public void createMusic(Song song, Genre genre) {
        Music music = Music.builder()
                .genre(genre.getGenre())
                .uri(song.getURI())
                .title(song.getTitle())
                .lyrics(song.getLyrics())
                .hasLyrics(song.getLyrics().length() > 0)
                .artists(song.getArtists())
                .acousticness(song.getAcousticness())
                .danceability(song.getDanceability())
                .energy(song.getEnergy())
                .instrumentalness(song.getInstrumentalness())
                .key(song.getKey())
                .liveness(song.getLiveness())
                .loudness(song.getLoudness())
                .speechiness(song.getSpeechiness())
                .tempo(song.getTempo())
                .valence(song.getValence())
                .build();
        dynamoDBMapper.save(music);
    }

    /**
     * If a song exists and it has no lyrics attribute, tries to find
     * lyrics for associated song.
     * @param song  - song object
     * @param genre - genre object
     */
    public void updateLyrics(Song song, Genre genre) {
        Music music = getMusic(song.getURI(), genre.getGenre());
        if(music != null && !music.getHasLyrics()) {
            music.setLyrics(song.getLyrics());
            music.setHasLyrics(true);
            dynamoDBMapper.save(music);
        }
    }

    /**
     * Reports if a song exists in the DB.
     * @param uri   - string sort key for entity music
     * @param genre - string genre primary key for entity music
     * @return      - boolean signifier of existence
     */
    public boolean songExists(String uri, String genre) {
        return getMusic(uri, genre) != null;
    }

    /**
     * Queries the music DB for a song.
     * @param uri   - string sort key for entity music
     * @param genre - string genre primary key for entity music
     * @return      - entity object for song
     */
    public Music getMusic(String uri, String genre) {
        return dynamoDBMapper.load(Music.class, genre, uri);
    }

    /**
     * Deletes an entry for Music DB.
     * @param uri   - string sort key for entity music
     * @param genre - string genre primary key for entity music
     */
    public void deleteMusic(String uri, String genre) {
        dynamoDBMapper.delete(getMusic(uri, genre));
    }

    /**
     * Returns all songs of a given genre from the DB.
     * @param g - string genre
     * @return  - list of music entities
     */
    public List<Music> getGenre(String g) {
        DynamoDBQueryExpression<Music> query =
                new DynamoDBQueryExpression<>();
        Music hashKeyValues = Music.builder().genre(g).build();
        query.setHashKeyValues(hashKeyValues);
        return dynamoDBMapper.query(Music.class, query);
    }

    public void fillEmptyLyrics() {
        ThreadPool threadPool = new ThreadPool(taskMax, threadMax);
        log.info("Thread pool instantiating.");
        for(String g : GenreConstants.genres) {
            try {
                threadPool.execute( () -> {
                    log.info("Starting lyrics update for genre " + g + ".");
                    Map<String, AttributeValue> lyrics = new HashMap<>();
                    lyrics.put(":noLyrics", new AttributeValue().withN("0"));
                    DynamoDBQueryExpression<Music> query =
                            new DynamoDBQueryExpression<>();
                    query.setHashKeyValues(Music.builder().genre(g).build());
                    query.withFilterExpression("hasLyrics = :noLyrics")
                            .withExpressionAttributeValues(lyrics)
                            .withLimit(100)
                            .withConsistentRead(false);
                    List<Music> noLyrics = dynamoDBMapper.query(Music.class, query);
                    log.info("Updating lyrics for " + noLyrics.size() + " in genre " + g + ".");
                    for(Music m : noLyrics) {
                        m.setLyrics(geniusClient.getLyrics(m.getTitle(), m.getArtists()));
                        m.setHasLyrics((m.getLyrics().length() > 0));
                        dynamoDBMapper.save(m);
                    }
                    log.info("Completed updating lyrics for " + g + ".");
                });
            } catch(Exception e) {
                log.error("Task in thread pool failed with exception ", e);
            }
        }
        threadPool.waitForCompletion();
        threadPool.stop();
        log.info("Thread pool terminated.");
    }
}
