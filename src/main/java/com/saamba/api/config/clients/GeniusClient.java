package com.saamba.api.config.clients;

import com.saamba.api.config.ClientConfig;
import com.saamba.api.enums.ClientTypes;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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

    @PostConstruct
    public GeniusClient init() {
        geniusClient = new OkHttpClient()
                .newBuilder()
                .build();
        return this;
    }

    public String getLyrics(String title, List<String> artists) {
        String url = getApiPath(title, artists);
        if(url.length() > 0) {
            url = parseLyrics(url);
        } else {
            url = "";
        }
        return url;
    }

    protected String parseLyrics(String url) {
        Document lyricsPage;
        String text = "";
        try {
            lyricsPage = Jsoup.connect(url).userAgent(userAgent).get();
            Elements lyricsDiv = lyricsPage.select(".lyrics");
            if(!lyricsDiv.isEmpty()) {
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
                text = builder.toString().replaceAll("<br> ", " ");
            }
        } catch (IOException e) {
            log.error("Failed to parse lyrics for " + url + ".", e);
        }
        return text;
    }

    protected String getApiPath(String title, List<String> artists) {
        String path = "";
        Request request = new Request.Builder()
                .url(getUrl(title, artists))
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Response response;
        try {
            response = geniusClient.newCall(request).execute();
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
        } catch(IOException | JSONException e) {
            log.error("Failed to retrieve API path for " + title
                    + " " + artists + ".", e);;
        }
        return path;
    }

    protected String getUrl(String title, List<String> artists) {
        StringBuilder sb = new StringBuilder();
        sb.append(apiHost).append(searchRequest).append(title);
        for(String str : artists)
            sb.append(" ").append(str);
        return sb.toString();
    }

    @Override
    public ClientTypes getClientType() { return ClientTypes.Genius; }

    @Override
    public void refreshCredentials() { }


}
