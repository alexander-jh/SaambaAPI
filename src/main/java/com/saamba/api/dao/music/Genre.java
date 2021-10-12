package com.saamba.api.dao.music;

import com.ibm.watson.tone_analyzer.v3.model.ToneScore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A DAO POJO is necessary for the sake of creating a properly formatted
 * JSON to match discovery client query logic.
 *
 * This representation is meant to handle data level logic in the application
 * the entity is for normalizing songs to match the DDB schema. More
 * detail in the music entity class.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private String genre;
    private List<Song> songs;
    private List<ToneScore> tones;

    public Genre(String genre) {
        this.genre = genre;
        this.songs = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Genre{ genre=").append(this.genre)
                .append(", artists=[");
        for(Song s : this.songs)
            sb.append(s.toString()).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");
        return sb.toString();
    }
}
