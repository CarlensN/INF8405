package com.example.myapplication.Class;
import android.graphics.Point;
import android.util.Pair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

public class LevelPresenter {
    private Stack<Pair<Point, Integer>> moves;
    private LevelView levelView;
    public Level level;
    public LevelPresenter(LevelView view){
        this.levelView = view;
        level = new Level();
        moves = new Stack<>();
    }
    public void updateLevel(InputStream is){
        level.updateLevel(is);
        ArrayList<BlockInfo> blocksInfo = level.getBlocksInfo();
        levelView.placeBlocks(blocksInfo);

    }
    public int[][] getMap(){
        return level.getMap();
    }

    public void addToMoves(Pair move){
        level.getMoves().push(move);
    }
}
