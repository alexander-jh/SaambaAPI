package com.saamba.api.dao.music;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class Playlist {

    private String[] trackUris;
    private List<String> concepts;
    private Map<String, Double> tones;

    public Playlist(String[] trackUris, List<String> concepts, Map<String, Double> tones) {
        this.trackUris = trackUris;
        this.concepts = concepts;
        this.tones = tones;
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        //Object to JSON in file
//        mapper.writeValue(new File("c:\\user.json"), this);

        //Object to JSON in String
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Could not convert playlist to JSON";
        }
    }

}
