package com.saamba.api.config;

import com.saamba.api.enums.ClientTypes;

public interface ClientConfig {
    public ClientTypes getClientType();
    public void refreshCredentials();
}
