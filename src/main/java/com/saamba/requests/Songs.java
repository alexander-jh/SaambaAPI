package com.saamba.requests;

import com.saamba.helpers.Constants;
import com.saamba.types.Song;
import com.saamba.wrappers.SpotifyClient;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.HashSet;;
import java.util.Set;

public class Songs {
    private Set<Song> songs;

    public Songs(String a, String id) {
        songs = new HashSet<>();
        try {
            Track[] tracks = SpotifyClient.getClient()
                    .getArtistsTopTracks(id, Constants.USA_CODE)
                    .build()
                    .execute();
            parseSongs(tracks, a);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            //System.out.println("Error: " + e.printStackTrace());
        }
    }

    public Set<Song> getSongs() { return this.songs; }

    private void parseSongs(Track[] tracks, String a) {
        for(Track t : tracks) {
            songs.add(new Song(t.getName(), a));
        }
    }
}
