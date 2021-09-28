package com.saamba.api.config;

import com.saamba.api.enums.ClientTypes;

/**
 * Interface for different clients and config wrappers for
 * each API and SDK called. Right now functions are trivial
 * but exists for future implementation.
 */
public interface ClientConfig {
    /**
     * Gets enum type for client
     * @return      - enum of client type
     */
    public ClientTypes getClientType();

    /**
     * Updates relevant credentials for each client.
     */
    public void refreshCredentials();
}
