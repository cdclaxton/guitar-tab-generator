package com.github.cdclaxton.sheetmusic;

import com.github.cdclaxton.music.Bar;

import java.util.List;

public class Section {

    private String name;
    private String text;
    private List<Bar> bars;

    public Section(String name, String text, List<Bar> bars) {
        this.name = name;
        this.text = text;
        this.bars = bars;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public List<Bar> getBars() {
        return bars;
    }
}
