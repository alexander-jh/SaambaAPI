package com.saamba.api.entity.user;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Users")
public class User {

    @DynamoDBHashKey(attributeName = "accountName")
    private String accountName;

    @DynamoDBAttribute(attributeName = "uri")
    private String uri;

    @DynamoDBAttribute(attributeName = "spotifyId")
    private String spotifyId;

    @DynamoDBAttribute(attributeName = "artists")
    private List<String> artists;

    @DynamoDBAttribute(attributeName = "genres")
    private List<String> genres;

    @DynamoDBAttribute(attributeName = "playlists")
    private List<Playlist> playlists;

    @DynamoDBAttribute(attributeName = "tweets")
    private List<Tweet> tweets;

}
