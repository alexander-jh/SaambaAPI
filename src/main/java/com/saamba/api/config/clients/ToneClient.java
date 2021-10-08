package com.saamba.api.config.clients;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.tone_analyzer.v3.model.ToneScore;
import com.saamba.api.config.ClientConfig;
import com.saamba.api.enums.ClientTypes;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.*;

public class ToneClient implements ClientConfig {

    @Value("${client.ibm.tone.key}")
    private String apiKey;

    @Value("${client.ibm.tone.url}")
    private String apiUrl;

    @Value("${client.ibm.tone.date}")
    private String apiDate;

    private ToneAnalyzer toneAnalyzer;

    public ToneClient() { }

    /**
     * Necessary function which instantiates after the no-arg constructor
     * is invoked. Needed since values aren't injected until after creation
     * of the class.
     * @return      - tone analyzer agent
     */
    @PostConstruct
    public ToneClient init() {
        this.toneAnalyzer = new ToneAnalyzer(apiDate,
                new IamAuthenticator.Builder().apikey(apiKey).build());
        this.toneAnalyzer.setServiceUrl(apiUrl);
        return this;
    }

    @Override
    public ClientTypes getClientType() {
        return ClientTypes.IBM;
    }

    @Override
    public void refreshCredentials() {
        // Not necessary
    }

    private final List<String> toneList = Arrays.asList("openness", "anger", "disgust", "fear", "sadness",
            "joy", "conscientiousness", "extroversion", "emotional range",
            "analytical", "confident", "tentative", "agreeableness" );

    /**
     * Returns the primary tone from a string of text.
     * @param text  - string of text
     * @return      - string of tone
     */
    public String analyzeText(String text) {
        return toneAnalyzer.tone(new ToneOptions.Builder().text(text).build())
                .execute()
                .getResult()
                .getDocumentTone()
                .getTones()
                .get(0)
                .getToneName();
    }
    /**
     * Returns the proportions of tones from a list of text.
     * @param l         - a list of text from users tweets
     * @return          - a normalized map of tones of the list of tweets
     */
    public Map<String, Double> analyzeList(List<String> l){
        Map<String, Double> mp = new HashMap<>();
        fillMap(mp);
        int i = 0;
        // iterate over all the tweets
        while(i<l.size()){
            List<ToneScore> ts = toneAnalyzer.tone(new ToneOptions.Builder().text(l.get(i)).build())
                    .execute()
                    .getResult()
                    .getDocumentTone()
                    .getTones();
            // iterate over all the tones in a single tweet
            for(int j =0; j<ts.size();j++){
                mp.replace(ts.get(j).getToneName(), mp.get(ts.get(j).getToneName()+ts.get(j).getScore()));
            }
        }
        normMap(mp);
        return mp;
    }

    /**
     * Fills a map with default values
     * @param mp         - map to be filled with default values
     */
    private void fillMap(Map<String, Double> mp){
        for (String s :toneList ) {
            mp.put(s, 0.0);
        }
    }

    /**
     * Normalizes the values in a map.
     * @param mp         - map to have its values normalized
     */
    private void normMap(Map<String, Double> mp){
        //sum up current values
        Double sum = 0.0;
        for (String s :toneList ) {
            sum+= mp.get(s);
        }
        // now replace old values with normalized values
        for (String s :toneList ) {
            mp.replace(s, mp.get(s)/sum);
        }
    }

}
