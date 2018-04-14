package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.*;

import java.util.ArrayList;
import java.util.List;

public class SheetMusicTransposition {

    /**
     * Transpose sheet music.
     *
     * @param sheetMusic Sheet music to transpose.
     * @param newKey Key to transpose to.
     * @param up Transpose up?
     * @param maxFretNumber Maximum fret number.
     * @return Transposed sheet music.
     * @throws InvalidKeyException
     * @throws InvalidChordException
     * @throws TranspositionException
     */
    public static SheetMusic transpose(SheetMusic sheetMusic, String newKey, boolean up, int maxFretNumber)
            throws InvalidKeyException, InvalidChordException, TranspositionException {

        // Create a new header
        Header newHeader = sheetMusic.getHeader();
        //newHeader.setKey(new Key(newKey));

        // Transpose each of the sections
        List<Section> sections = sheetMusic.getSections();
        for (int i = 0; i < sections.size(); i++) {
            sections.set(i, SheetMusicTransposition.transposeSection(sections.get(i),
                    sheetMusic.getHeader().getKey().getKey(), newKey, up, maxFretNumber));
        }

        // Instantiate and return the transposed sheet music
        return new SheetMusic(newHeader, sheetMusic.getMetadata(), sections);
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
     * @throws InvalidChordException
     * @throws TranspositionException
     */
    static Section transposeSection(Section section, String currentKey, String newKey, boolean up,
                                    int maxFretNumber) throws InvalidChordException, TranspositionException {

        // Transpose each of the bars to the new key
        List<Bar> bars = new ArrayList<>();
        for (int i = 0; i < section.getBars().size(); i++) {
            bars.add(BarTransposition.transposeBar(section.getBars().get(i), currentKey, newKey,
                    up, maxFretNumber));
        }

        // Instantiate and return a new section
        return new Section(section.getName(), section.getText(), bars);
    }

}
