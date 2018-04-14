package com.github.cdclaxton.guitartabgenerator.sheetmusic;

import com.github.cdclaxton.guitartabgenerator.music.Bar;

import java.util.ArrayList;
import java.util.List;

public class Section {

    private String name;
    private List<String> text = new ArrayList<>();
    private List<Bar> bars = new ArrayList<>();

    public Section() { }

    public Section(String name, List<String> text, List<Bar> bars) {
        this.name = name;
        this.text = text;
        this.bars = bars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getText() {
        return text;
    }

    public void addText(String text) {
        this.text.add(text);
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void addBar(Bar bar) {
        this.bars.add(bar);
    }
}
