package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.*;

import java.util.ArrayList;
import java.util.List;

public final class SheetMusicTransposition {

    /**
     * Transpose sheet music.
     *
     * @param sheetMusic Sheet music to transpose.
     * @param newKey Musical key to transpose to.
     * @param up Transpose up?
     * @param maxFretNumber Maximum fret number.
     * @return Transposed sheet music.
     * @throws InvalidKeyException Invalid key.
     * @throws InvalidChordException Invalid chord.
     * @throws TranspositionException Unable to transpose the music.
     */
    public static SheetMusic transpose(final SheetMusic sheetMusic,
                                       final String newKey,
                                       final boolean up,
                                       final int maxFretNumber)
            throws InvalidKeyException, InvalidChordException, TranspositionException {

        // Create a new header (just the key changes)
        Header currentHeader = sheetMusic.getHeader();
        Header newHeader = new Header(currentHeader.getTitle(), currentHeader.getArtist(), new Key(newKey),
                currentHeader.getTimeSignature());

        // Transpose each of the sections
        List<Section> currentSections = sheetMusic.getSections();
        List<Section> newSections = new ArrayList<>(currentSections.size());
        for (int i = 0; i < currentSections.size(); i++) {
            newSections.add(SheetMusicTransposition.transposeSection(currentSections.get(i),
                    sheetMusic.getHeader().getKey().getKey(), newKey, up, maxFretNumber));
        }

        // Instantiate and return the transposed sheet music
        return new SheetMusic(newHeader, sheetMusic.getMetadata(), newSections);
    }

    /**
     * Transpose a section to a new key.
     *
     * @param section Section to transpose.
     * @param currentKey Current key.
     * @param newKey New key.
     * @param up Transpose up?
     * @param maxFretNumber Maximum fret number.
     * @return Transposed section
     * @throws InvalidChordException Invalid chord.
     * @throws TranspositionException Unable to transpose section.
     */
    private static Section transposeSection(final Section section,
                                            final String currentKey,
                                            final String newKey,
                                            final boolean up,
                                            final int maxFretNumber)
            throws InvalidChordException, TranspositionException {

        // Transpose each of the bars to the new key
        List<Bar> bars = new ArrayList<>(section.getBars().size());
        for (int i = 0; i < section.getBars().size(); i++) {
            bars.add(BarTransposition.transposeBar(section.getBars().get(i), currentKey, newKey, up, maxFretNumber));
        }

        // Instantiate and return a new section
        return new Section(section.getName(), section.getText(), bars);
    }

}
