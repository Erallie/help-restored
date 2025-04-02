package com.gozarproductions.utils;

import java.util.List;

public class HelpPageInfo {
    public final List<String> lines;
    public final List<String> preambleLines;
    public final int entriesPerPage;
    public final int totalPages;

    public HelpPageInfo(List<String> lines, List<String> preambleLines, int entriesPerPage, int totalPages) {
        this.lines = lines;
        this.preambleLines = preambleLines;
        this.entriesPerPage = entriesPerPage;
        this.totalPages = totalPages;
    }
}
