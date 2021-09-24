package com.saamba.api.entity.user;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.saamba.api.entity.user.Playlist;
import com.saamba.api.entity.user.Tweet;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
