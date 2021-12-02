package com.saamba.api.config.clients;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v1.Discovery;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResponse;
import com.ibm.watson.discovery.v1.model.QueryOptions;
import com.ibm.watson.discovery.v1.model.QueryResult;

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
     * @param block      - string for query
     *
     * @return          - uri reference to spotify song
     */
    public List<String> scanEmployees(String[] block) {
        log.info("Starting query " + buildQueryString(block) + " over Discovery collection.");

        QueryResponse queryResponse = discoveryClient.query(
                (new QueryOptions.Builder(envId, collectionId))
                        .query(buildQueryString(block))
                .build())
                .execute()
                .getResult();

        List<String> employees = new ArrayList<>();

        for(QueryResult result : queryResponse.getResults()) {
            employees.add(result.get("employeeId").toString());
            String  str = result.get("documents").toString();
            for(String b: block){
                int i = str.indexOf(b);
                if(i!=-1 && i>25){
                    log.info("Employee " + result.get("employeeId").toString() + ": " + str.substring(i-50, i+50));
                }
            }
        }
        return employees;
    }

    private String buildQueryString(String[] list) {
        StringBuilder query = new StringBuilder();
        for(String s : list)
            query.append(s).append(' ');
        return query.toString();
    }
}
