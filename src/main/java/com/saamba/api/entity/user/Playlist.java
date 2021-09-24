package com.saamba.api.entity.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;
import java.util.List;

@DynamoDBDocument
public class Playlist implements Serializable {
    private String date;
    private List<String> songs;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
}
