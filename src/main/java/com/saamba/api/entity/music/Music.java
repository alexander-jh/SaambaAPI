package com.saamba.api.entity.music;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Music")
public class Music implements Serializable {

    @DynamoDBHashKey(attributeName = "genre")
    private String genre;

    @DynamoDBRangeKey(attributeName = "uri")
    private String uri;

    @DynamoDBAttribute(attributeName = "title")
    private String title;

    @DynamoDBAttribute(attributeName = "hasLyrics")
    private Boolean hasLyrics;

    @DynamoDBAttribute(attributeName = "lyrics")
    private String lyrics;

    @DynamoDBAttribute(attributeName = "artists")
    private List<String> artists;

    @DynamoDBAttribute(attributeName = "acousticness")
    private Float acousticness;

    @DynamoDBAttribute(attributeName = "danceability")
    private Float danceability;

    @DynamoDBAttribute(attributeName = "energy")
    private Float energy;

    @DynamoDBAttribute(attributeName = "instrumentalness")
    private Float instrumentalness;

    @DynamoDBAttribute(attributeName = "key")
    private Integer key;

    @DynamoDBAttribute(attributeName = "liveness")
    private Float liveness;

    @DynamoDBAttribute(attributeName = "loudness")
    private Float loudness;

    @DynamoDBAttribute(attributeName = "speechiness")
    private Float speechiness;

    @DynamoDBAttribute(attributeName = "tempo")
    private Float tempo;

    @DynamoDBAttribute(attributeName = "valence")
    private Float valence;
}