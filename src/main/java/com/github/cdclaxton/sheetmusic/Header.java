package com.github.cdclaxton.sheetmusic;

import com.github.cdclaxton.music.Key;

public class Header {

    private String title;
    private String artist;
    private Key key;

    public Header() {}

    /**
     * Instantiate the header section of a piece of sheet music.
     *
     * @param title Song title.
     * @param artist Artist or composer.
     * @param key Key the song is in.
     */
    public Header(String title, String artist, Key key) {
        this.title = title;
        this.artist = artist;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Key getKey() {
        return key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
