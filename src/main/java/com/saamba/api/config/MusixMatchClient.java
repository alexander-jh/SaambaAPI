package com.saamba.api.config;

import com.saamba.api.enums.ClientTypes;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MusixMatchClient implements ClientConfig {

    @Value("${client.musixmatch.token}")
    String accessToken;

    MusixMatch musixClient;

    MusixMatchClient() { }

    @PostConstruct
    public MusixMatchClient init() {
        musixClient = new MusixMatch(accessToken);
        return this;
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.MusixMatch; }

    @Override
    public void refreshCredentials() { }

    public String getLyrics(String artist, String song) {
        //try {
            int trackID = getTrackId(song, artist);
            if(trackID != -1) {
                //Lyrics l = musixClient.getLyrics(trackID);
                //return l.getLyricsBody();
                return "" + trackID;
            }
        //} catch(MusixMatchException e) {
            //System.out.println("Error: " + e.getMessage());
        //}
        return "";
    }

    private int getTrackId(String artist, String song) {
        int id = -1;
        try {
            Track track = musixClient.getMatchingTrack(song, artist);
            TrackData data = track.getTrack();
            id = data.getTrackId();
        } catch(MusixMatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return id;
    }
}
