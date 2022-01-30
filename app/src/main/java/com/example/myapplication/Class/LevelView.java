package com.example.myapplication.Class;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class LevelView extends RelativeLayout {
    public LevelPresenter levelPresenter;
    ArrayList<Block> blocks = new ArrayList<>();
    ImageView mapView;
    BlockFactory blockFactory;
    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blockFactory = new BlockFactory(context);
    }

    public void setBlockSize(int blockSize) {
        blockFactory.setBlockSize(blockSize);
    }
    public void displayMap(Context context){
        mapView = new ImageView(context);
        int blockSize = blockFactory.getBlockSize();
        mapView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.map, null));
        mapView.setLayoutParams(new RelativeLayout.LayoutParams(6 * blockSize , 6* blockSize));
        this.addView(mapView);
        mapView.setTranslationX(blockSize);
        mapView.setTranslationY(blockSize);
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

    public void clearBlocks(){
        for(Block block:blocks){
            this.removeView(block);
        }
        this.blocks.clear();
    }

    public void setLevelPresenter(LevelPresenter levelPresenter) {
        this.levelPresenter = levelPresenter;
    }


    public void undoMove(Pair<Integer, Integer> moveToUndo) {
        int blockId = moveToUndo.first;
        int pos = moveToUndo.second;
        for(Block block : blocks){
            if(block.getBlockId() == blockId){
                block.undoMove(pos);
            }
        }
    }
}
