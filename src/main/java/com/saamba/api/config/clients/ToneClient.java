package com.saamba.api.config.clients;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import com.saamba.api.config.ClientConfig;
import com.saamba.api.enums.ClientTypes;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class ToneClient implements ClientConfig {

    @Value("${client.ibm.tone.key}")
    private String apiKey;

    @Value("${client.ibm.tone.url}")
    private String apiUrl;

    @Value("${client.ibm.tone.date}")
    private String apiDate;

    private ToneAnalyzer toneAnalyzer;

    public ToneClient() { }

    @PostConstruct
    public ToneClient init() {
        this.toneAnalyzer = new ToneAnalyzer(apiDate,
                new IamAuthenticator.Builder().apikey(apiKey).build());
        this.toneAnalyzer.setServiceUrl(apiUrl);
        return this;
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.IBM; }

    @Override
    public void refreshCredentials() {}

    public String analyzeTest(String text) {
        return toneAnalyzer.tone(new ToneOptions.Builder().text(text).build())
                .execute()
                .getResult()
                .getDocumentTone()
                .getTones()
                .get(0)
                .getToneName();
    }
}
