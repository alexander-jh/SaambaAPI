package com.saamba.wrappers;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.BearerTokenAuthenticator;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class DiscoverySearch {
    private static String key = "W4yRKE--NoJJPHVMiOmu9LCWp5spX6Mqh5vbMi9mNlYT";
//    private static String url = "https://api.kr-seo.discovery.watson.cloud.ibm.com/instances/f9f78f79-0354-4f71-95d1-cae2f2f2bbe7";
    private static String url = "https://api.eu-gb.discovery.watson.cloud.ibm.com";
    //    private static ToneAnalyzer t = new ToneAnalyzer("2019-04-30",
//            new IamAuthenticator.Builder().apikey(key).build());

    public static ArrayList<String[]> findSongs(String str) {

        Discovery discovery = new Discovery("2019-04-30", new IamAuthenticator.Builder().apikey(key).build());
        discovery.setServiceUrl(url);

        String environmentId = "1545d966-b84a-4b43-aa4f-3ea72e60c85d";
        String collectionId = "b56e44f2-f9e6-45cf-a756-d3ba136575de";

        // query document
        System.out.println("Querying the collection...");
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
//        queryBuilder.query("field:value");
        queryBuilder.naturalLanguageQuery("Happy");
        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute().getResult();

        // Parses QueryResponse and puts Artist + Title in an ArrayList of Arrays [Artist, Title]
        ArrayList<String[]> songs = new ArrayList<>();
        List<QueryResult> results = queryResponse.getResults();

        for (QueryResult result : results){
            songs.add(new String[]{result.get("artist").toString(), result.get("title").toString()});
        }

        return songs;
    }

    public static void main(String args[]) {
//        String analysis = Tone.analyze(findSongs(TwitterTesting.getPinnedTweet("iamcardib")));
//
//        System.out.println("Tone of the Twitter account: " + analysis);

        System.out.println(findSongs("Happy").get(1)[0]);
//          findSongs("Happy");
    }
}
