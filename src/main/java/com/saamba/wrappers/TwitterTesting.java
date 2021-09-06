package com.saamba.wrappers;

import java.util.List;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.signature.TwitterCredentials;

public class TwitterTesting {
    private static TwitterClient twitterClient;
    //twitter credentials
    private static final String twitterAPIKey = "6kjRr6R2aUhIiLruVqJmkMqSm";
    private static final String twitterAPISecret = "jnDafGcWLCv5kPKDI251o2PcElCym2QaUn5MYcatHwRUGCDgan";
    private static final String twitterBearerToken = "AAAAAAAAAAAAAAAAAAAAAEe7TQEAAAAAGFax%2ByQ0JeH3%2F7ShLhC7gutWZ8k%3DoSsYxSUyCdgybGwkPlJiaAyNuV1sIBRnG6Fd94kWVufWZhvxE3";
    private static final String twitterAccessToken = "764298631931432960-CI6gcp7LljnvczIw39dmJPZKOilOsT8";
    private static final String twitterAccessTokenSecret = "scYrIE4CANo8kgKu8DhTU6N1mHeHmAQBXyUxL3n5EGrOg";

    public static void main(String args[]) {
        twitterClient = new TwitterClient(TwitterCredentials.builder()
                .accessToken(twitterAccessToken)
                .accessTokenSecret(twitterAccessTokenSecret)
                .apiKey(twitterAPIKey).apiSecretKey(twitterAPISecret).build());

        User ye = twitterClient.getUserFromUserName("kanyewest");
        System.out.println(ye.getFollowersCount());
        // System.out.println(ye.getPinnedTweet().getText());
        AdditionalParameters ap = AdditionalParameters.builder()
                .recursiveCall(false).maxResults(20).build();
        TweetList t = twitterClient.getUserTimeline(ye.getId(), ap);
        List<TweetData> td = t.getData();
        for (int i = 0; i < 15; i++) {
            System.out.println(td.get(i).getText());
        }

    }
}
