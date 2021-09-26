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
import java.util.List;

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

    }

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

    public List<Tweet> getTweets(String accountName) {
        Tweet t;
        List<Tweet> tweets = new ArrayList<>();
        for(TweetV2.TweetData td : twitterClient.getUserTimeline(
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
}
