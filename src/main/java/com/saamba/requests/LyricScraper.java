package com.saamba.requests;

import com.saamba.helpers.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LyricScraper {

    private final String artist;
    private final String title;

    public LyricScraper(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    public String getLyrics() {
        Document html = null;
        String lyrics = "";

        try {
            html = getHTML();
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        if(html != null) lyrics = parseHTML(html);

        return lyrics;
    }

    private Document getHTML() throws IOException {
        Document html;

        String artistFormatted = (artist.contains(" ")) ?
                artist.replace(' ', '-') :
                artist;

        String songFormatted = (title.contains(" ")) ?
                title.replace(' ', '-') :
                title;

        artistFormatted = artistFormatted.replaceAll("[^a-zA-Z0-9-]", "");
        songFormatted = songFormatted.replaceAll("[^a-zA-Z0-9-]", "");

        html = Jsoup.connect((new StringBuilder())
                        .append(Constants.GENIUS_URL)
                        .append(artistFormatted)
                        .append('-')
                        .append(songFormatted)
                        .append('-')
                        .append("lyrics")
                        .toString())
                .get();
        return html;
    }

    private String parseHTML(Document html) {
        StringBuilder lyrics = new StringBuilder();

        Elements first = html.select("div.lyrics");

        for(Element e : first) {
            String text = e.text();
            if(text.charAt(0) != '<')
                lyrics.append(text);
        }

        return lyrics.toString();
    }
}
