package com.saamba.api.dao.music;

import java.util.ArrayList;
import java.util.List;

import com.saamba.api.entity.music.Music;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;

public class Song {

    private final String title;
    private final String uri;
    private String lyrics;
    private List<String> artists;
    private final Float acousticness;
    private final Float danceability;
    private final Float energy;
    private final Float instrumentalness;
    private final Integer key;
    private final Float liveness;
    private final Float loudness;
    private final Float speechiness;
    private final Float tempo;
    private final Float valence;

    public Song(TrackSimplified track, AudioFeatures features) {
        this.artists = new ArrayList<>();
        this.title = track.getName();
        this.uri = track.getUri();
        for(ArtistSimplified a : track.getArtists())
            this.artists.add(a.getName());
        this.lyrics = "";
        this.acousticness = features.getAcousticness();
        this.danceability = features.getDanceability();
        this.energy = features.getEnergy();
        this.instrumentalness = features.getInstrumentalness();
        this.key = features.getKey();
        this.liveness = features.getLiveness();
        this.loudness = features.getLoudness();
        this.speechiness = features.getSpeechiness();
        this.tempo = features.getTempo();
        this.valence = features.getValence();
    }

    public Song(Music music) {
        this.artists = new ArrayList<>();
        this.title = music.getTitle();
        this.uri = music.getUri();
        this.artists.addAll(music.getArtists());
        this.lyrics = "";
        this.acousticness = music.getAcousticness();
        this.danceability = music.getDanceability();
        this.energy = music.getEnergy();
        this.instrumentalness = music.getInstrumentalness();
        this.key = music.getKey();
        this.liveness = music.getLiveness();
        this.loudness = music.getLoudness();
        this.speechiness = music.getSpeechiness();
        this.tempo = music.getTempo();
        this.valence = music.getValence();
    }

    public Song(Track track, AudioFeatures features) {
        this.artists = new ArrayList<>();
        this.title = track.getName();
        this.uri = track.getUri();
        for(ArtistSimplified a : track.getArtists())
            this.artists.add(a.getName());
        this.lyrics = "";
        this.acousticness = features.getAcousticness();
        this.danceability = features.getDanceability();
        this.energy = features.getEnergy();
        this.instrumentalness = features.getInstrumentalness();
        this.key = features.getKey();
        this.liveness = features.getLiveness();
        this.loudness = features.getLoudness();
        this.speechiness = features.getSpeechiness();
        this.tempo = features.getTempo();
        this.valence = features.getValence();
    }

    public Float getAcousticness() {
        return acousticness;
    }

    public Float getDanceability() {
        return danceability;
    }

    public Float getEnergy() {
        return energy;
    }

    public Float getInstrumentalness() {
        return instrumentalness;
    }

    public Integer getKey() {
        return key;
    }

    public Float getLiveness() {
        return liveness;
    }

    public Float getLoudness() {
        return loudness;
    }

    public Float getSpeechiness() {
        return speechiness;
    }

    public Float getTempo() {
        return tempo;
    }

    public Float getValence() {
        return valence;
    }

    public List<String> getArtists() {
        return this.artists;
    }


    public String getTitle() {
        return this.title;
    }

    public String getURI() {
        return this.uri;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return (new StringBuilder())
                .append("title=").append(this.title)
                .append(", uri=").append(this.uri)
                .append(", lyrics=\"").append(this.lyrics)
                .append("\"}").toString();
    }
}
