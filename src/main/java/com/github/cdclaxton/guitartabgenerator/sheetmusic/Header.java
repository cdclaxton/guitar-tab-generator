package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.Bar;
import com.github.cdclaxton.guitartabgenerator.music.Key;

import java.util.Objects;

public final class Header {

    private final String title;
    private final String artist;
    private final Key key;
    private final Bar.TimeSignature timeSignature;

    /**
     * Instantiate the header section of a piece of sheet music.
     *
     * @param title Song title.
     * @param artist Artist or composer.
     * @param key Key the song is in.
     * @param timeSignature Time signature of the music.
     */
    public Header(final String title, final String artist, final Key key, final Bar.TimeSignature timeSignature) {
        this.title = title;
        this.artist = artist;
        this.key = Key.newInstance(key);
        this.timeSignature = timeSignature;
    }

    /**
     * Get the title.
     *
     * @return Title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the artist.
     *
     * @return Artist.
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Get the musical key.
     *
     * @return Musical key.
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get the time signature.
     *
     * @return Time signature.
     */
    public Bar.TimeSignature getTimeSignature() {
        return timeSignature;
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
