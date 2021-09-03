package com.saamba.types;

import com.saamba.requests.LyricScraper;
import org.json.simple.JSONObject;

public class Song {
    private final String title;
    private final String lyrics;

    public Song(String title, String a) {
        this.title = title;
        LyricScraper ls = new LyricScraper(a, title);
        this.lyrics = ls.getLyrics();
    }

    public String getTitle() { return title; }

    public String getLyrics() {
        return lyrics;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        if(title.length() > 0 && lyrics.length() > 0) {
            json.put("Title", title);
            json.put("Lyrics", lyrics);
        }
        return json;
    }
}
