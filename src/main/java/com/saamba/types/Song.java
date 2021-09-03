package com.saamba.types;

import com.saamba.requests.LyricScraper;

public class Song {
    private final String title;
    private final String lyrics;

    public Song(String title, String a) {
        this.title = "";
        LyricScraper ls = new LyricScraper(a, title);
        this.lyrics = ls.getLyrics();
        System.out.println(a);
        System.out.println(this.title);
        System.out.println(this.lyrics);
    }

    public String getTitle() { return title; }

    public String getLyrics() {
        return lyrics;
    }
}
