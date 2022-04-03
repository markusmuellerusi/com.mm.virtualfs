package com.mm.virtualfs;

import java.util.ArrayList;

public interface IOutput {
    void writeLine(String text);
    void clear();
    ArrayList<String> getLines();
}

