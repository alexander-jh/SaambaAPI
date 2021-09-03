package com.saamba;

import com.saamba.requests.Genres;
import com.saamba.types.Genre;

import java.util.ArrayList;
import java.util.List;

public class Scraper {

    public static void main(String[] args) {
        List<Genre> genres = new ArrayList<>();
        String[] g = (new Genres()).getGenres();
        for(String s : g)
            genres.add(new Genre(s));
    }
}
