package com.mm.virtualfs;

import java.util.ArrayList;


public class Output implements IOutput{

    @Override
    public void writeLine(String text) {
        System.out.println(text);
    }

    public void clear(){}

    @Override
    public ArrayList<String> getLines() { return new ArrayList<>(); }
}

