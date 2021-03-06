package com.saamba.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.saamba.api.enums.ClientTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig implements  ClientConfig {

    @Value("${client.aws.accesskey}")
    private String accessKey;

    @Value("${client.aws.secretkey}")
    private String secretKey;

    @Value("${region.aws.default}")
    private String region;

    @Override
    public ClientTypes getClientType() { return ClientTypes.AWS; };

    @Override
    public void refreshCredentials() {};

    /**
     * Mapper to DDB client for CRUD operations.
     * @return      - DDB mapper
     */
    @Bean
    public DynamoDBMapper mapper() {
        return new DynamoDBMapper(amazonDynamoDBConfig());
    }

    /**
     * Creates and authenticates DDB client to the relevant AWS account.
     * TODO: authenticate to resource directly
     * @return      - DDB client
     */
    private AmazonDynamoDB amazonDynamoDBConfig() {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(RegionUtils.getRegion(region).toString())
                .build();
    }
}
