package com.saamba.api.dao.music;

import java.util.ArrayList;
import java.util.List;

import com.saamba.api.entity.music.Music;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    private String title;
    private String uri;
    private String lyrics;
    private List<String> artists;
    private Float acousticness;
    private Float danceability;
    private Float energy;
    private Float instrumentalness;
    private Integer key;
    private Float liveness;
    private Float loudness;
    private Float speechiness;
    private Float tempo;
    private Float valence;

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
        this.lyrics = music.getLyrics();
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

    @Override
    public String toString() {
        return (new StringBuilder())
                .append("title=").append(this.title)
                .append(", uri=").append(this.uri)
                .append(", lyrics=\"").append(this.lyrics)
                .append("\"}").toString();
    }
}
