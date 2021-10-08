package com.saamba.api.config;

import com.saamba.api.enums.ClientTypes;
import com.saamba.api.entity.user.Tweet;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.signature.TwitterCredentials;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TwitterConfig implements ClientConfig {

    @Value("${client.twitter.results.max}")
    private int maxResults;

    @Value("${client.twitter.accesskey}")
    private String accessKey;

    @Value("${client.twitter.secretkey}")
    private String secretKey;

    @Value("${client.twitter.bearer.token}")
    private String bearerToken;

    @Value("${client.twitter.accesstoken}")
    private String accessToken;

    @Value("${client.twitter.accesssecret}")
    private String accessTokenSecret;

    protected TwitterClient twitterClient;

    public TwitterConfig() { }

    /**
     * Necessary function which instantiates after the no-arg constructor
     * is invoked. Needed since values aren't injected until after creation
     * of the class.
     * @return      - twitter agent
     */
    @PostConstruct
    public TwitterConfig init() {
        this.twitterClient = new TwitterClient(
                TwitterCredentials.builder().accessToken(accessToken)
                        .accessTokenSecret(accessTokenSecret)
                        .apiKey(accessKey)
                        .apiSecretKey(secretKey)
                        .build());
        return this;
    }

    @Override
    public ClientTypes getClientType() {
        return ClientTypes.Twitter;
    }

    @Override
    public void refreshCredentials() {
        // Undefined
    }

    /**
     * Gets pinned tweet for an account name
     * @param accountName   - string twitter handle
     * @return              - string of pinned tweet
     */
    public String getPinnedTweet(String accountName) {
        String pinned = "";
        try {
            pinned = twitterClient.getUserFromUserName(accountName)
                    .getPinnedTweet()
                    .getText();
        } catch(Exception e) {
            log.error("Account " + accountName + " has no pinned tweet.");
        }
        return pinned;
    }

    /**
     * Returns a list of all tweets associated with a twitter handle. The
     * current limitation is that this call assumes a profiles must
     * be public.
     * @param accountName   - string twitter handle
     * @return              - a list of tweet objects
     */
    private List<Tweet> getTweets(String accountName) {
        Tweet t;
        List<Tweet> tweets = new ArrayList<>();
        for (TweetV2.TweetData td : twitterClient.getUserTimeline(
                twitterClient.getUserFromUserName(accountName).getId(),
                AdditionalParameters.builder()
                        .recursiveCall(false)
                        .maxResults(maxResults)
                        .build()
        ).getData()) {
            t = new Tweet();
            t.setDate(td.getCreatedAt().toString());
            t.setMessage(td.getText());
            tweets.add(t);
        }
        return tweets;
    }

    /**
     *
     * @param accountName   - string twitter handle
     * @return              - a list of the text from tweets
     */
    public List<String> tweetTexts(String accountName){
        List<Tweet> twl = getTweets(accountName);
        List<String> l = new ArrayList<>();
        int i = 0;
        while(i<twl.toArray().length){
            Tweet t = twl.get(i);
            l.add(t.getMessage());
            i++;
        }
        return l;
    }

    // TODO: populate map
    private Map<String, String> artistMap = new HashMap<>();
    /**
     *
     * @param accountName   - string twitter handle
     * @return              - a list of the accounts they are following
     */
    public List<String> getFollowingList(String accountName){
        List<String> uL = twitterClient.getFollowersIds(twitterClient.getUserFromUserName(accountName).getId());
        List<String> uL2 = new ArrayList<>();
        for (String s: uL
             ) {
            if(artistMap.containsKey(twitterClient.getUserFromUserId(s).getName())) {
                uL2.add(twitterClient.getUserFromUserId(s).getName());
            }
        }
        return uL2;
    }

}
