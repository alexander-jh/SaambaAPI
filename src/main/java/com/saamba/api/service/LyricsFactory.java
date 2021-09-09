package com.saamba.api.service;

import com.saamba.api.jython.LyricType;

import org.python.core.Py;
import org.python.core.PyObject;

import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class LyricsFactory {

    private PyObject lyricDownloader;
    private PySystemState systemState;

    public LyricsFactory() {
        setSystemState();
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from LyricDownloader import LyricDownloader");
        lyricDownloader = interpreter.get("LyricDownloader");
    }

    public LyricType create(String token, String artist, String song) {
        PyObject lyricObject = lyricDownloader.__call__(new PyString(token),
                new PyString(artist), new PyString(song));
        return (LyricType) lyricObject.__tojava__(LyricType.class);
    }

    private void setSystemState() {
        systemState = Py.getSystemState();
        systemState.path.append(new PyString("target/jython"));
    }
}
