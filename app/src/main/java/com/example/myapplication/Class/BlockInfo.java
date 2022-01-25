package com.example.myapplication.Class;

public class BlockInfo {
    private char type;
    private int x;
    private int y;
    private int nUnits;
    public BlockInfo(char type, int x, int y, int nUnits){
        this.type = type;
        this.x = x;
        this.y = y;
        this.nUnits = nUnits;
    }

    public char getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getnUnits() {
        return nUnits;
    }
}
