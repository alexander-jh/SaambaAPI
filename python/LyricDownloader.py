import os
import sys
import lyricsgenius

class LyricDownloader:

    def __init__(self, token, artist, song):
        self.song = song
        self.artist = artist
        self.genius = lyricsgenius.Genius(token)

    def getLyrics(self):
        devnull = open(os.devnull, "w")
        out = sys.stdout 
        sys.stdout = devnull
        ele = self.genius.search_artist(self.artist, max_songs=0)
        song = None if ele is None else ele.song(self.song)
        sys.stdout = out
        return "" if song is None else song.lyrics


client = LyricDownloader(sys.argv[1], sys.argv[2], sys.argv[3])
lyrics = client.getLyrics()
print(lyrics)
