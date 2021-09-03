package com.saamba;

import com.saamba.helpers.ThreadPool;
import com.saamba.requests.Genres;
import com.saamba.types.Genre;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Scraper {

    public static void main(String[] args) throws Exception {
        ThreadPool pool = new ThreadPool();
        String[] g = (new Genres()).getGenres();

        for(String s : g)
            pool.execute( () -> {
                FileWriter file = null;
                JSONObject json = (new Genre(s)).toJSON();
                try{
                    file = new FileWriter(s + ".json");
                    file.write(json.toJSONString());
                } catch(IOException e) {
                    System.out.println("Error: " + e.getMessage());
                } finally {
                    try {
                        if(file != null) {
                            file.flush();
                            file.close();
                        }
                    } catch(IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            });

        pool.waitForCompletion();
        pool.stop();
    }
}
