package com.saamba.types;

import com.saamba.requests.LyricScraper;
import org.json.simple.JSONObject;

public class Song {
    private final String title;
    private final String lyrics;

    public Song(String title, String a) {
        this.title = "";
        LyricScraper ls = new LyricScraper(a, title);
        this.lyrics = ls.getLyrics();
    }

    public String getTitle() { return title; }

    public String getLyrics() {
        return lyrics;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(title, lyrics);
        return json;
    }
}
