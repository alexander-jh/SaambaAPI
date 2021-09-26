package com.saamba.api.dao.music;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.saamba.api.entity.music.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SongToMusic {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

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

    public void updateLyrics(Song song, Genre genre) {
        Music music = getMusic(song.getURI(), genre.getGenre());
        if(music != null && !music.getHasLyrics()) {
            music.setLyrics(song.getLyrics());
            music.setHasLyrics(true);
            dynamoDBMapper.save(music);
        }
    }

    public boolean songExists(String uri, String genre) {
        return getMusic(uri, genre) != null;
    }

    public Music getMusic(String uri, String genre) {
        return dynamoDBMapper.load(Music.class, genre, uri);
    }

    public void deleteMusic(String uri, String genre) {
        dynamoDBMapper.delete(getMusic(uri, genre));
    }

    public List<Music> getGenre(String g) {
        DynamoDBQueryExpression<Music> query =
                new DynamoDBQueryExpression<>();
        Music hashKeyValues = new Music();
        hashKeyValues.setGenre(g);
        query.setHashKeyValues(hashKeyValues);
        return dynamoDBMapper.query(Music.class, query);
    }
}
