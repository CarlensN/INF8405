package com.example.myapplication.Class;

import android.graphics.Point;

import java.util.ArrayList;

public class Level {
    int currentLevel;
    ArrayList<Point> blockPositions;

    public int getCurrentLevel() {
        return currentLevel;
    }

    public ArrayList<Point> getBlockPositions() {
        return blockPositions;
    }

    public void updateBlockPositions(){

    }
}
