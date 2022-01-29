package com.example.myapplication.Class;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelPresenter {
    private LevelView levelView;
    public Level level;
    public LevelPresenter(LevelView view){
        this.levelView = view;
        level = new Level();
    }
    public void updateLevel(InputStream is){
        level.updateLevel(is);
        ArrayList<BlockInfo> blocksInfo = level.getBlocksInfo();
        levelView.placeBlocks(blocksInfo);

    }
    public int[][] getMap(){
        return level.getMap();
    }
}
