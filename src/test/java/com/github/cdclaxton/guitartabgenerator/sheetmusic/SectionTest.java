package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @Test
    void constructorIsImmutable() throws InvalidTimingException, InvalidChordException, InvalidStringException, InvalidFretNumberException {
        String name = "section-name";

        List<String> text = new ArrayList<>();
        text.add("Line-1");
        text.add("Line-2");

        Bar bar1 = new BarBuilder(Bar.TimeSignature.Four4)
                .addTimedChord(0, "C#m")
                .addNote(1, 0, 0)
                .build();
        List<Bar> bars = new ArrayList<>();
        bars.add(bar1);

        Section section = new Section(name, text, bars);
        assertEquals(name, section.getName());
        assertEquals(text, section.getText());
        assertEquals(bars, section.getBars());

        // Mutate the text and check the section isn't mutated
        text.add("Line-3");
        List<String> expectedText = new ArrayList<>();
        expectedText.add("Line-1");
        expectedText.add("Line-2");
        assertEquals(expectedText, section.getText());

        // Mutate the bars and check the section isn't mutated
        Bar bar2 = new BarBuilder(Bar.TimeSignature.Four4)
                .addTimedChord(0, "E")
                .addNote(2, 0, 0)
                .build();
        bars.add(bar2);

        List<Bar> expectedBars = new ArrayList<>();
        expectedBars.add(bar1);
        assertEquals(expectedBars, section.getBars());
    }

}