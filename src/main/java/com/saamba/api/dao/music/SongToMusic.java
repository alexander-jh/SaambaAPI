package com.saamba.api.dao.music;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.saamba.api.entity.music.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts DAO POJO for a song and creates the entity object
 * for CRUD and DB related actions.
 */
@Component
public class SongToMusic {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

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
        Music hashKeyValues = new Music();
        hashKeyValues.setGenre(g);
        query.setHashKeyValues(hashKeyValues);
        return dynamoDBMapper.query(Music.class, query);
    }
}
