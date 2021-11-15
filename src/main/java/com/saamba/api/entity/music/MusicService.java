package com.saamba.api.entity.music;

import com.amazonaws.AbortedException;
import com.saamba.api.dao.music.Genre;
import com.saamba.api.dao.music.Song;
import com.saamba.api.dao.music.SongToMusic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MusicService {

    @Autowired
    private SongToMusic musicDao;

    /**
     * CRUD operations.
     */
    public void createMusic(Song song, Genre genre) {
        musicDao.createMusic(song, genre);
    }

    public Music getMusic(String uri, String genre) {
        return musicDao.getMusic(uri, genre);
    }

    public void updateLyrics(Song song, Genre genre) {
        musicDao.updateLyrics(song, genre);
    }

    public boolean songExists(String uri, String genre) {
        return musicDao.songExists(uri, genre);
    }

    public void deleteMusic(String uri, String genre) {
        musicDao.deleteMusic(uri, genre);
    }

    public void fillEmptyLyrics() { musicDao.fillEmptyLyrics(); }

    /**
     * Converts music entity objects for each song by genre into
     * DAO song representation for exporting to JSON.
     * @param g - string genre
     * @return  - genre object
     */
    public Genre exportGenre(String g) {
        Genre genre = new Genre(g);
        try {
            List<Music> music = musicDao.getGenre(g);
            List<Song> songs = new ArrayList<>();
            if(music == null) return genre;
            for (Music m : music)
                songs.add(new Song(m));
            genre.setSongs(songs);
        } catch(AbortedException e) {
            log.error("Failed to get songs for genre " + g + ".");
        }
        return genre;
    }
}
