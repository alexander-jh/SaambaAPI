package com.saamba.api.config;


import com.saamba.api.enums.ClientTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "client.ibm")
public class IBMClient implements ClientConfig {

    @Value("${accesskey}")
    private String accessKey;

    @Override
    public ClientTypes getClientType() { return ClientTypes.IBM; };

    @Override
    public void refreshCredentials() {};
}
