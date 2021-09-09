package com.saamba.api.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;
import java.util.List;

@DynamoDBTable(tableName = "users")
public class User implements Serializable {

    @DynamoDBHashKey
    private String accountName;

    private String uri;
    private String spotifyId;
    private List<String> blackList;
    private List<String> artists;
    private List<String> genres;
    private List<Playlist> playlists;
    private List<Tweet> tweets;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public List<String> getArtists() {
        return artists;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getUri() {
        return uri;
    }
}
