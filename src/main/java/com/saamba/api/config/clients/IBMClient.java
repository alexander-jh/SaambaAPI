package com.saamba.api.config.clients;


import com.saamba.api.config.ClientConfig;
import com.saamba.api.enums.ClientTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IBMClient implements ClientConfig {

    @Value("${client.ibm.accesskey}")
    private String accessKey;

    @Override
    public ClientTypes getClientType() { return ClientTypes.IBM; }

    @Override
    public void refreshCredentials() {}
}
