package com.saamba.api.service;

import com.saamba.api.dao.LyricType;

import org.python.core.PyObject;

import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LyricsFactory {

    @Value("${client.genius.accesstoken}")
    private String geniusToken;

    private PyObject lyricDownloader;

    public LyricsFactory() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from LyricDownloader import LyricDownloader");
        lyricDownloader = interpreter.get("LyricDownloader");
    }

    public LyricType create(String artist, String song) {
        PyObject lyricObject = lyricDownloader.__call__(new PyString(artist),
                new PyString(song));
        return (LyricType) lyricObject.__tojava__(LyricType.class);
    }
}
