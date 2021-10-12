package com.saamba.api.config.clients;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResponse;

import com.ibm.watson.discovery.v1.model.QueryResult;
import com.saamba.api.config.AppConfig;
import com.saamba.api.config.ClientConfig;
import com.saamba.api.enums.ClientTypes;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DiscoveryClient implements ClientConfig {

    @Value("${client.ibm.discovery.key}")
    private String apiKey;

    @Value("${client.ibm.discovery.url}")
    private String apiUrl;

    @Value("${client.ibm.discovery.date}")
    private String apiDate;

    @Value("${client.ibm.discovery.envid}")
    private String envId;

    @Value("${client.ibm.discovery.colid}")
    private String collectionId;

    private Discovery discoveryClient;

    public DiscoveryClient() { }

    @Override
    public ClientTypes getClientType() { return ClientTypes.IBM; }

    @Override
    public void refreshCredentials() {}

    /**
     * Necessary function which instantiates after the no-arg constructor
     * is invoked. Needed since values aren't injected until after creation
     * of the class.
     * @return      - discovery query agent
     */
    @PostConstruct
    public DiscoveryClient init() {
        this.discoveryClient = new Discovery(apiDate,
                new IamAuthenticator.Builder()
                        .apikey(apiKey)
                        .build());
        this.discoveryClient.setServiceUrl(apiUrl);
        return this;
    }

    /**
     * Searches for song in discovery client based upon the seed query
     * string.
     * @param seed      - string for query
     * @return          - uri reference to spotify song
     */
    public ArrayList<String[]> findSongs(String seed) {
        log.info("Starting query over Discovery collection for " + seed + " .");
        QueryResponse query = discoveryClient.query(
                        (new QueryOptions.Builder(envId, collectionId))
                        .naturalLanguageQuery(seed)
                        .build())
                .execute()
                .getResult();
        // Parses QueryResponse and puts Artist + Title in an ArrayList of Arrays [Artist, Title]
        ArrayList<String[]> songs = new ArrayList<>();
        List<QueryResult> results = query.getResults();

        for (com.ibm.watson.discovery.v1.model.QueryResult result : results){
            songs.add(new String[]{result.get("artist").toString(), result.get("title").toString()});
        }

        return songs;
    }
}
