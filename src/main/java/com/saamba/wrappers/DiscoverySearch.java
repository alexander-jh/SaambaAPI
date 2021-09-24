package com.saamba.wrappers;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.BearerTokenAuthenticator;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v2.model.QueryResult;

public class DiscoverySearch {
    private static String key = "BWC92BJnFoUgNaYZd1fBcAj1Zy1vaujX65d-RMbW6cpR";
//    private static String url = "https://api.kr-seo.discovery.watson.cloud.ibm.com/instances/f9f78f79-0354-4f71-95d1-cae2f2f2bbe7";
    private static String url = "https://api.kr-seo.discovery.watson.cloud.ibm.com";
    //    private static ToneAnalyzer t = new ToneAnalyzer("2019-04-30",
//            new IamAuthenticator.Builder().apikey(key).build());

    public static String findSongs(String str) {

        Discovery discovery = new Discovery("2019-04-30", new IamAuthenticator.Builder().apikey(key).build());
        discovery.setServiceUrl("https://api.kr-seo.discovery.watson.cloud.ibm.com");

        String environmentId = "53023a27-586f-403c-8443-df13cd7fdbcb";
        String collectionId = "f491a294-9f6d-455c-aa2c-f3790ae0a37c";

        // query document
        System.out.println("Querying the collection...");
        QueryOptions.Builder queryBuilder = new QueryOptions.Builder(environmentId, collectionId);
//        queryBuilder.query("field:value");
        queryBuilder.naturalLanguageQuery("Happy");
        QueryResponse queryResponse = discovery.query(queryBuilder.build()).execute().getResult();

        // print out the results
        System.out.println("Query Results:");
        System.out.println(queryResponse);

        return "";
    }

    public static void main(String args[]) {
//        String analysis = Tone.analyze(findSongs(TwitterTesting.getPinnedTweet("iamcardib")));
//
//        System.out.println("Tone of the Twitter account: " + analysis);

        System.out.println(findSongs("Happy"));

    }
}
