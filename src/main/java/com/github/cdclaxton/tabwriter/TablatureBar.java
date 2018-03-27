package com.github.cdclaxton.tabwriter;

import java.util.List;

public class TablatureBar {

    private String ruler;
    private String chordLine;
    private List<String> tabLines;

    public TablatureBar(String ruler, String chordLine, List<String> tabLines) {
        this.ruler = ruler;
        this.chordLine = chordLine;
        this.tabLines = tabLines;
    }

    public String getRuler() {
        return ruler;
    }

    public String getChordLine() {
        return chordLine;
    }

    public List<String> getTabLines() {
        return tabLines;
    }
}
