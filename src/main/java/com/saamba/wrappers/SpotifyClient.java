package com.saamba.wrappers;

import java.io.IOException;
import org.apache.hc.core5.http.ParseException;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
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

    public static String createPlaylist (ArrayList<String[]> songs){
        // Gets the Song uris
        String[] trackUris = searchSongs(songs);

        // Create Empty Playlist

        // Add songs to the playlist
        for(int i = 0; i < trackUris.length; i++){
            // Add song to playlist
            addSongToPlayist(trackUris[i], "PLAYLIST LINK");
        }

        return ""; // return playlist link
    }

    private static String[] searchSongs(ArrayList<String[]> songs){
       String[] trackUris = new String[songs.size()];
       int skips = 0;

       for(int i = 0; i < songs.size(); i++){
           String uri = searchSong(songs.get(i)[0] + ", " + songs.get(i)[1]);

           if (uri.length() != 0){
               trackUris[i - skips] = uri;
           }
           else {
               skips++;
           }
       }

       return trackUris;
    }

    private static String searchSong(String song){
        SearchItemRequest searchItemRequest = getClient().searchItem(song, ModelObjectType.TRACK.getType())
//          .market(CountryCode.SE)
//          .limit(10)
//          .offset(0)
//          .includeExternal("audio")
                .build();
        String uri = "";

        try {
            final SearchResult searchResult = searchItemRequest.execute();

            uri =  searchResult.getTracks().getItems()[0].getUri();

//            System.out.println("Song Uri: " + searchResult.getTracks().getItems()[0].getUri());
        } catch (IOException | SpotifyWebApiException | ArrayIndexOutOfBoundsException | ParseException e) {
            System.out.println("Query: " + song);
            System.out.println("Error: " + e.getMessage());
        }

        return uri;
    }

    private static void addSongToPlayist(String uri, String playlist){
        // TODO
    }

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
//        createPlaylist_Sync();
        String[] songsToAdd = searchSongs(DiscoverySearch.findSongs("Joy"));
    }

}
