package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.Bar;
import com.github.cdclaxton.guitartabgenerator.music.Key;

import java.util.Objects;

public class Header {

    private String title;
    private String artist;
    private Key key;
    private Bar.TimeSignature timeSignature;

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

    public Bar.TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(Bar.TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(title, header.title) &&
                Objects.equals(artist, header.artist) &&
                Objects.equals(key, header.key) &&
                timeSignature == header.timeSignature;
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, artist, key, timeSignature);
    }

}
