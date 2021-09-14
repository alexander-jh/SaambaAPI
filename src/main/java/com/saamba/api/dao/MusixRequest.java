package com.saamba.api.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MusixRequest {
    private TrackList track_list;

    public TrackList getTrack_list() {
        return track_list;
    }

    public void setTrack_list(TrackList track_list) {
        this.track_list = track_list;
    }
}
