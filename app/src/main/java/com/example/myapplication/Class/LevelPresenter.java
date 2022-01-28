package com.example.myapplication.Class;
import java.io.InputStream;
import java.util.ArrayList;

public class LevelPresenter {
    LevelView levelView;
    Level level;
    public LevelPresenter(LevelView view){
        this.levelView = view;
        level = new Level();
    }
    public void updateLevel(InputStream is){
        level.updateLevel(is);
        ArrayList<BlockInfo> blocksInfo = level.getBlocksInfo();
        levelView.placeBlocks(blocksInfo);

    }

    public void blockHTouched(BlockH block) {
        block.setBoundaries(level.findBoundariesX(block));
    }
    public void blockVTouched(BlockV block) {
        block.setBoundaries(level.findBoundariesY(block));
    }

    public void onTouchUp(){

    }
}
