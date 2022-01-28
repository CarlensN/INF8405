package com.example.myapplication.Class;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class BlockH extends Block {
    //block position
    float x = 0;
    float offsetX = 0;
    int boundaryLeft;
    int boundaryRight;

    public BlockH(Context context, int nUnits, int blockSize) {
        super(context, blockSize);
        this.nUnits = nUnits;
        this.setLayoutParams(new RelativeLayout.LayoutParams(nUnits * blockSize , blockSize));
    }

    @Override
    public void setBoundaries(Pair<Integer, Integer> boundaries) {
        this.boundaryLeft = boundaries.first;
        this.boundaryRight = boundaries.second;
    }

    @Override
    protected void touchDown(MotionEvent motionEvent) {
        offsetX = motionEvent.getX();
        x = this.getTranslationX();
        levelPresenter.blockHTouched(this);
    }

    @Override
    public void touchMove(MotionEvent motionEvent) {
        x += motionEvent.getX() - this.offsetX;
        if(x < boundaryLeft * blockSize){
            x = boundaryLeft * blockSize;
        }else if(x + (nUnits -1) *blockSize > boundaryRight* blockSize){
            x = boundaryRight * blockSize - (nUnits - 1) * blockSize;
        }
        this.setTranslationX(x);

    }

    @Override
    protected void touchUp(MotionEvent motionEvent) {
        float adjustmentX = Math.round(x /blockSize) *blockSize;
        this.setTranslationX(adjustmentX);
        this.x = adjustmentX;

        int y = (int) this.getTranslationY() / blockSize;

        for (int i = 1; i < levelPresenter.level.getMap().length; i++){
            if (levelPresenter.level.getMap()[i][y] == getBlockId()){
                levelPresenter.level.getMap()[i][y] = - 1;
            }
        }
        int pointX = (int) (this.getTranslationX() / blockSize);
        for (int i = 0; i < nUnits; i++){
            levelPresenter.level.getMap()[(int) pointX + i][y] = getBlockId();
        }
    }


}

