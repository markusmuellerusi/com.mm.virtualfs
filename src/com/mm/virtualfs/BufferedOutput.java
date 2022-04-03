package com.mm.virtualfs;

import java.util.ArrayList;

public class BufferedOutput implements IOutput {

    public static ArrayList<String> buffer;

    public BufferedOutput(){
        buffer = new ArrayList<>();
    }

    @Override
    public void writeLine(String text) {
        System.out.println(text);
        buffer.add(text);
    }

    public void clear(){
        buffer.clear();
    }

    @Override
    public ArrayList<String> getLines() {
        return buffer;
    }
}
