package com.saamba.wrappers;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Credentials {
    // TODO: Decouple credential artifacts
    private static final String clientId = "3681033801214b1f9051438603d03bae";
    private static final String clientSecret = "d7020609582a48bcb7d31697c59ff147";

    private static final SpotifyApi api = new SpotifyApi.Builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .build();

    private static final ClientCredentialsRequest credentialRequest = api.clientCredentials()
            .build();

    public static String getAccessToken() {
        String accessToken = "";
        try {
            final ClientCredentials clientCredentials = credentialRequest.execute();
            accessToken = clientCredentials.getAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return accessToken;
    }
}
