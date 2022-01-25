package com.example.myapplication.Class;

import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;

public class LevelPresenter {
    ArrayList<Block> blocks = new ArrayList();
    LevelView levelView;
    Level level;
    public LevelPresenter(LevelView view){
        this.levelView = view;
        level = new Level();
    }

    public void blockAdded(){

    }
    public void updateLevel(InputStream is){
        level.updateLevel(is);
        ArrayList<BlockInfo> blocksInfo = level.getBlocksInfo();
        levelView.placeBlocks(blocksInfo);
    }
    public void retrieveLevelInfo(InputStream is){

    }
}
