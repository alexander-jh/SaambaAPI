package com.saamba.api.config.clients;

import com.saamba.api.config.ClientConfig;
import com.saamba.api.enums.ClientTypes;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Simply just a wrapper to implement the GET call to Genius to
 * search for a song by title and artists. Parses api_path for
 * a song, then uses OkHttp client to parse lyrics.
 */
@Component
@Slf4j
public class GeniusClient implements ClientConfig {

    @Value("${client.genius.api.search}")
    private String searchRequest;

    @Value("${client.genius.token}")
    private String accessToken;

    @Value("${client.genius.api.endpoint}")
    private String apiHost;

    @Value("${client.genius.api.url}")
    private String baseUrl;

    @Value("${client.html.user.agent}")
    private String userAgent;

    private OkHttpClient geniusClient;

    public GeniusClient() { }

    /**
     * Necessary function which instantiates after the no-arg constructor
     * is invoked. Needed since values aren't injected until after creation
     * of the class.
     * @return      - genius agent
     */
    @PostConstruct
    public GeniusClient init() {
        geniusClient = new OkHttpClient()
                .newBuilder()
                .build();
        return this;
    }

    /**
     * Entry point for client. Gets lyrics for a song. If the lyrics
     * don't exist or there is a failure just returns an empty string
     * to caller.
     * @param title     - string song title
     * @param artists   - list of string artist names
     * @return          - string of associated song lyrics
     */
    public String getLyrics(String title, List<String> artists) {
        String url = getApiPath(title, artists);
        if(url.length() > 0) {
            url = parseLyrics(url);
        } else {
            url = "";
        }
        return url;
    }

    /**
     * Http parser for song's Genius page. Navigates directly to lyrics
     * div and breaks apart based upon <br>. Logic can be followed in the
     * regex calls.
     * @param url       - string url to genius page
     * @return          - string of lyrics
     */
    protected String parseLyrics(String url) {
        Document lyricsPage;
        String text = "";
        try {
            lyricsPage = Jsoup.connect(url).userAgent(userAgent).get();
            Element lyricsDiv = lyricsPage.getElementById("lyrics-root");
//            System.out.println(lyricsDiv);
            if(lyricsDiv != null) {
                text = Jsoup.clean(lyricsDiv.html(), Whitelist.none()
                        .addTags("br")).trim();
                Pattern pattern = Pattern.compile("\\[.+\\]");
                StringBuilder builder = new StringBuilder();
                for (String line : text.split("<br> ")) {
                    String strippedLine = line.replaceAll("\\s", "");
                    if (!pattern.matcher(strippedLine).matches() &&
                            !(strippedLine.isEmpty() && builder.length() == 0))
                        builder.append(
                                line.replaceAll("\\P{Print}", "")
                        );
                }
                if (builder.length() > 5)
                    builder.delete(builder.length() - 5, builder.length());
                text = builder.toString().replaceAll("<br>", " ");
            }
        } catch (IOException e) {
            log.error("Failed to parse lyrics for " + url + ".");
        }
        return text;
    }

    /**
     * Makes GET request to Genius API for the search endpoint using a
     * song's title and associated artists. Entry logic constructs the
     * request. Internal logic conducts request to Genius and parses
     * relevant JSON for api_path location. Last part constructs url
     * for song.
     * @param title     - string song title
     * @param artists   - list of string of artists
     * @return          - html for song
     */
    protected String getApiPath(String title, List<String> artists) {
        String path = "";
        Request request = new Request.Builder()
                .url(getUrl(title, artists))
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Response response;
        try {
            try {
                response = geniusClient.newCall(request).execute();
            } catch(InterruptedIOException e) {
                log.error("okhttp3 returned a null response for " + title + ".");
                return "";
            }
            if(response.isSuccessful()) {
                JSONObject json = new JSONObject(Objects.
                        requireNonNull(response.body()).string());
                JSONArray arr = json.getJSONObject("response")
                        .getJSONArray("hits");
                if(arr.length() > 0) {
                    path = arr.getJSONObject(0)
                            .getJSONObject("result")
                            .get("api_path")
                            .toString();
                    path = baseUrl + path;
                }
            }
        } catch(JSONException | IOException e) {
            log.error("Failed to retrieve API path for " + title
                    + " " + artists + ".", e);;
        }
        return path;
    }

    /**
     * Creates search query url for GET request.
     * @param title     - string song title
     * @param artists   - list of string of artists
     * @return          - url for API call
     */
    protected String getUrl(String title, List<String> artists) {
        StringBuilder sb = new StringBuilder();
        sb.append(apiHost).append(searchRequest).append(title);
        for(String str : artists)
            sb.append(" ").append(str);
        return sb.toString().replaceAll(" ", "%20")
                .replaceAll("\\[", "")
                .replaceAll("]", "");
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.Genius; }

    @Override
    public void refreshCredentials() {
        // Not necessary to implement.
    }


}
