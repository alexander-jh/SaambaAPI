package com.saamba.api.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saamba.api.enums.ClientTypes;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

@Component
@Slf4j
public class CredentialManager implements ClientConfig {

    @Value("${spring.aws.secretsmanager.secretname}")
    private String secretName;

    @Value("${spring.aws.secretsmanager.endpoint}")
    private String endPoint;

    @Value("${spring.aws.secretsmanager.region}")
    private String region;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode secretsJson;

    private AWSSecretsManager credentialClient;

    @PostConstruct
    public CredentialManager init() {
        this.credentialClient = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();
        ByteBuffer binarySecretData;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult getSecretValueResponse = null;
        try {
            getSecretValueResponse = this.credentialClient.getSecretValue(getSecretValueRequest);
        } catch(ResourceNotFoundException e) {
            log.error("The requested secret "  +  secretName + " was not found");
        } catch(InvalidRequestException e) {
            log.error("The request was invalid due to: " + e.getMessage());
        } catch(InvalidParameterException e) {
            log.error("The request had invalid params: "  +  e.getMessage());
        }
        if(getSecretValueResponse == null)
            return null;

        String secret = getSecretValueResponse.getSecretString();

        if(secret != null) {
            try {
                secretsJson  =  objectMapper.readTree(secret);
            } catch(IOException e) {
                log.error("Exception while retrieving secret values: "  +  e.getMessage());
            }
        } else {
            log.error("The Secret String returned is null");
            return null;
        }
        return this;
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.AWS; };

    @Override
    public void refreshCredentials() {};

    public String getSecretValue(String secretVal) {
        return secretsJson.get(secretVal).textValue();
    }
}
