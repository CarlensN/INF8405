package com.example.myapplication.Class;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class LevelView extends RelativeLayout {

    private LevelPresenter levelPresenter;
    ArrayList<Block> blocks = new ArrayList<>();
    BlockFactory blockFactory;
    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        levelPresenter = new LevelPresenter(this);
        blockFactory = new BlockFactory(context);
    }

    public void updateLevel(int id) throws IOException {
        InputStream is = getResources().openRawResource(id);
        levelPresenter.updateLevel(is);
    }

    public void setBlockSize(int blockSize) {
        blockFactory.setBlockSize(blockSize);
    }

    public void placeBlocks(ArrayList<BlockInfo> blocksInfo){
        for(BlockInfo blockInfo: blocksInfo) {
            Block block = blockFactory.createBlock(blockInfo.getType(),blockInfo.getnUnits());
            block.setBlockId(blockInfo.getId());
            this.addView(block);
            block.setTranslationX(blockInfo.getX() * blockFactory.getBlockSize());
            block.setTranslationY(blockInfo.getY() * blockFactory.getBlockSize());
            block.setLevelPresenter(levelPresenter);
            blocks.add(block);
        }
    }

    public void setLevelPresenter(LevelPresenter levelPresenter) {
        this.levelPresenter = levelPresenter;
    }


}
