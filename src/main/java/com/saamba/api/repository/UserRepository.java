package com.saamba.api.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.saamba.api.config.TwitterConfig;
import com.saamba.api.config.clients.DiscoveryClient;
import com.saamba.api.config.clients.ToneClient;
import com.saamba.api.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Service level logic to handle reporting back to main controller
 * and construct majority of API calls from front-end.
 */
@Repository("user")
public class UserRepository {

    @Autowired
    private DynamoDBMapper mapper;

    @Resource(name="discovery")
    private DiscoveryClient discoveryClient;

    @Resource(name="twitter")
    private TwitterConfig twitterConfig;

    @Resource(name="tone")
    private ToneClient toneClient;

    /**
     * Returns a JSON string of a playlist from a user's twitter handle.
     * @param accountName   - string twitter handle passed in url
     * @return              - formatted JSON string of playlist
     */
    public String getPlaylist(String accountName) {
        return discoveryClient.findSongs(
                toneClient.analyzeText(
                        twitterConfig.getPinnedTweet(
                                accountName)));
    }

    /**
     * Add a new user to the user table.
     * @param user      - JPA entity of user
     * @return          - returns the created user
     */
    public User addUser(User user) {
        mapper.save(user);
        return user;
    }

    /**
     * Queries users in table through primary key, twitter handle.
     * @param account   - string twitter handle
     * @return          - referenced user object representation
     */
    public User findUserByAccount(String account) {
        return mapper.load(User.class, account);
    }

    /**
     * Deletes user from table.
     * @param user      - user object reference to delete
     * @return          - returns confirmation of delete
     */
    public String deleteUser(User user) {
        mapper.delete(user);
        return "user removed !!";
    }

    /**
     * Pushes changes to a user to the table.
     * @param user      - updated user object
     * @return          - confirmation of update
     */
    public String editUser(User user) {
        mapper.save(user, buildExpression(user));
        return "record updated ...";
    }

    /**
     * Creates the DynamoDB save expression for updates to the user
     * object using DDB mapper.
     * @param user      - user object to update
     * @return          - returns the expression to execute to caller
     */
    private DynamoDBSaveExpression buildExpression(User user) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("accountName", new ExpectedAttributeValue(new AttributeValue().withS(user.getAccountName())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }
}
