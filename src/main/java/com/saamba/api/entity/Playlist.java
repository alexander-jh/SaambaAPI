package com.saamba.api.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.io.Serializable;
import java.util.List;

@DynamoDBDocument
public class Playlist implements Serializable {
    private String date;
    private List<String> songs;

    public void setDate(String date) {
        this.date = date;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public String getDate() {
        return date;
    }

    public List<String> getSongs() {
        return songs;
    }
}
