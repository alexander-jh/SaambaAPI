package com.saamba.api.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.config.clients.DiscoveryClient;
import com.saamba.api.config.clients.ToneClient;
import com.saamba.api.dao.music.Playlist;
import com.saamba.api.entity.employee.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service level logic to handle reporting back to main controller
 * and construct majority of API calls from front-end.
 */
@Slf4j
@Repository("user")
public class EmployeeRepository {

    @Autowired
    private DynamoDBMapper mapper;

    @Resource(name="discovery")
    private DiscoveryClient discoveryClient;

    @Resource(name="tone")
    private ToneClient toneClient;

    /**
     * Returns a JSON string of a playlist from a user's twitter handle.
     * @param accountName   - string twitter handle passed in url
     * @return              - formatted JSON string of playlist
     */
    public String getPlaylist(String accountName) {
        try {
            List<String> rawTweets = twitterConfig.tweetTexts(accountName);
            List<String> concepts = twitterConfig.getConcepts(rawTweets);
            List<String> tones = toneClient.getMaxTones(rawTweets);
            List<String> followers = twitterConfig.getFollowingList(accountName);

            //query using just top tone and one concept
            List<String[]> songsAndArtists = discoveryClient.findSongs(tones, concepts, followers);
            String[] trackUris = searchSongs(songsAndArtists);
            return playlistoJSON(new Playlist(trackUris, concepts, tones, followers));
        } catch(Exception e) {
            log.error("Something bad happened.");
            return "";
        }
    }

    private String playlistToJSON(Playlist p) {
        String s = "";
        if(p == null || p.getTrackUris().length == 0) return s;
        ObjectMapper mapper = new ObjectMapper();
        try {
            s = mapper.writeValueAsString(p);
        } catch(IOException e) {
            log.error("Playlist has failed to parse into json.", e);
        }
        return s;
    }

    /**
     * Add a new user to the user table.
     * @param user      - JPA entity of user
     * @return          - returns the created user
     */
    public Employee addUser(Employee user) {
        mapper.save(user);
        return user;
    }

    /**
     * Queries users in table through primary key, twitter handle.
     * @param account   - string twitter handle
     * @return          - referenced user object representation
     */
    public Employee findUserByAccount(String account) {
        return mapper.load(Employee.class, account);
    }

    /**
     * Deletes user from table.
     * @param user      - user object reference to delete
     * @return          - returns confirmation of delete
     */
    public String deleteUser(Employee user) {
        mapper.delete(user);
        return "user removed !!";
    }

    /**
     * Pushes changes to a user to the table.
     * @param user      - updated user object
     * @return          - confirmation of update
     */
    public String editUser(Employee user) {
        mapper.save(user, buildExpression(user));
        return "record updated ...";
    }

    public String[] searchSongs(List<String[]> songs){
        String[] trackUris = new String[songs.size()];
        int skips = 0;
        for(int i = 0; i < songs.size(); i++) {
            String uri = spotifyClient.searchSong(songs.get(i)[0] + ", " + songs.get(i)[1]);
            if (uri.length() != 0)
                trackUris[i - skips] = uri;
            else
                skips++;
        }
        return trackUris;
    }

    private void addSongToPlayist(String uri, String playlist){
        // TODO
    }

    /**
     * Creates the DynamoDB save expression for updates to the user
     * object using DDB mapper.
     * @param user      - user object to update
     * @return          - returns the expression to execute to caller
     */
    private DynamoDBSaveExpression buildExpression(Employee user) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("accountName", new ExpectedAttributeValue(new AttributeValue().withS(user.getAccountName())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }
}
