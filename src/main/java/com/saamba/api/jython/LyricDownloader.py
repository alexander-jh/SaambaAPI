from com.saamba.api.jython import LyricType
import lyricsgenius

class LyricDownloader:
    def __init__(self, token, artist, song):
        self.song = song
        self.artist = artist
        self.genius = lyricgenius.Genius(token)

    def getLyrics(self):
        lyrics = self.genius(f'{self.artist} {self.song}')
        lyrics