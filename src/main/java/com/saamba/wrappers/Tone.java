package com.saamba.wrappers;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

public class Tone {
    private static String key = "VT8rLBabKfAk6k5rWo7hgxtbt55lJEXH10nbM-mfINRG";
    private static String url = "https://api.au-syd.tone-analyzer.watson.cloud.ibm.com/instances/40fa6de1-a614-494d-8df5-de90ed5d30a6";
    private static ToneAnalyzer t = new ToneAnalyzer("2017-09-21",
            new IamAuthenticator.Builder().apikey(key).build());

    public static String analyze(String str) {
        t.setServiceUrl(url);
        ToneOptions toneop = new ToneOptions.Builder().text(str).build();
        ToneAnalysis toneAnalysis = t.tone(toneop).execute().getResult();
        String s = toneAnalysis.getDocumentTone().getTones().get(0)
                .getToneName();
        return s;
    }

    public static void main(String args[]) {
        String analysis = analyze(
                TwitterTesting.getPinnedTweet("taylorswift13"));
        System.out.println(analysis);
    }

}
