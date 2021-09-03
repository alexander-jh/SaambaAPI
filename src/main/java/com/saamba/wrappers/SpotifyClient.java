package com.saamba.wrappers;

import com.wrapper.spotify.SpotifyApi;

public class SpotifyClient {
    private static final String accessToken = Credentials.getAccessToken();
    private static final SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();

    public static SpotifyApi getClient() {
        return api;
    }
}
