package com.saamba;

import com.saamba.helpers.SaambaLog;
import com.saamba.helpers.ThreadPool;
import com.saamba.requests.Genres;
import com.saamba.types.Genre;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Scraper {

    public static void main(String[] args) throws Exception {
        SaambaLog logger = new SaambaLog(Scraper.class);
        ThreadPool pool = new ThreadPool();
        String[] g = (new Genres()).getGenres();
        logger.info("Starting scraping of genres.");
        for(String s : g)
            pool.execute( () -> {
                FileWriter file = null;
                JSONObject json = (new Genre(s)).toJSON();
                try{
                    file = new FileWriter("json/" + s + ".json");
                    file.write(json.toJSONString());
                    logger.info("JSON Created: json/" + s + ".json");
                } catch(IOException e) {
                    logger.error(e.getMessage());
                } finally {
                    try {
                        if(file != null) {
                            file.flush();
                            file.close();
                        }
                    } catch(IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            });
        logger.info("Scraper successfully completed.");
        pool.waitForCompletion();
        pool.stop();
    }
}
