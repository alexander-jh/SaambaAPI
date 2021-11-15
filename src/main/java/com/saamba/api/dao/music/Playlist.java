package com.saamba.api.dao.music;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Playlist {

    private String[] trackUris;
    private List<String> concepts;
    private List<String> tones;
    private List<String> followers;

    public Playlist(String[] trackUris, List<String> concepts, List<String> tones, List<String> followers) {
        this.trackUris = trackUris;
        this.concepts = new ArrayList<>();
        this.concepts.add(concepts.get(0));
        this.concepts.add(concepts.get(1));
        this.concepts.add(concepts.get(2));
        this.tones = tones;
        this.followers = followers;
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