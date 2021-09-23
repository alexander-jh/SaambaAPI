package com.saamba.wrappers;

import java.io.IOException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

public class SpotifyClient {
    private static final String secret = "d7020609582a48bcb7d31697c59ff147";
    private static final String accessToken = Credentials.getAccessToken();
    private static final SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(accessToken).build();
    private static final String userId = "sxidjpj1e228sikk7qy8ukvhs";

    public static SpotifyApi getClient() {
        return api;
    }

    private static final CreatePlaylistRequest createPlaylistRequest = api
            .createPlaylist(userId, "new playlist")
//          .collaborative(false)
//          .public_(false)
//          .description("Amazing music.")
            .build();

    public static void createPlaylist_Sync() {
        Playlist playlist = null;
        try {
            playlist = createPlaylistRequest.execute();
            System.out.println("Name: " + playlist.getName());
        } catch (IOException | SpotifyWebApiException
                | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // no way to test the following code currently but i think this is how it would work
        SearchTracksRequest t = api.searchTracks("22").build();
        Paging<Track> tp = null;
        try {
            tp = t.execute();
        } catch (IOException | SpotifyWebApiException
                | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        Track[] o = tp.getItems();
        String[] o2 = { o[0].getUri() };
        api.addItemsToPlaylist(playlist.getId(), o2);
    }

    public static void main(String args[]) {
        createPlaylist_Sync();
    }

}
