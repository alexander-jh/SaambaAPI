package com.saamba.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LyricsFactory {

    private static final String source = "python3";
    private static final String path = "/home/ajw/Dropbox/OSU/CSE5914 - Capstone/SaambaAPI/target/python/LyricDownloader.py";

    ProcessBuilder process;

    public LyricsFactory() {
        process = new ProcessBuilder().inheritIO();
    }

    public String processLyrics(String token, String artist, String song) {
        StringBuilder lyrics = new StringBuilder();
        process.command(source, path, token, artist, song);
        try {
            Process p = process.start();
            p.waitFor();
            BufferedReader in = new BufferedReader((new InputStreamReader(p.getInputStream())));
            String line;
            while((line = in.readLine()) != null)
                lyrics.append(line);
        } catch(IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return lyrics.toString();
    }
}
