package com.saamba.api.dao.music;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    private String[] trackUris;
    private List<String> concepts;
    private List<String> tones;
    private List<String> followers;

}
