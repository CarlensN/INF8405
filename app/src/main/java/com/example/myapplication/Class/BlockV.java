package com.example.myapplication.Class;

import android.content.Context;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class BlockV extends Block{
    //block position
    float y = 0;
    float offsetY = 0;
    int boundaryTop;
    int boundaryBot;

    public BlockV(Context context,int nUnits, int blockSize) {
        super(context,blockSize);
        this.nUnits = nUnits;
        this.setLayoutParams(new RelativeLayout.LayoutParams(blockSize, nUnits * blockSize));
    }

    @Override
    public void setBoundaries(Pair<Integer, Integer> boundaries) {
        this.boundaryTop = boundaries.first;
        this.boundaryBot = boundaries.second;
    }

    @Override
    protected void touchDown(MotionEvent motionEvent) {
        this.offsetY = motionEvent.getY();
        this.y = this.getTranslationY();
        levelPresenter.blockVTouched(this);
    }

    @Override
    public void touchMove(MotionEvent motionEvent) {
        y += motionEvent.getY() - this.offsetY;
        if(y < boundaryTop * blockSize){
            y = boundaryTop* blockSize;
        }else if(y + (nUnits -1) *blockSize > boundaryBot * blockSize){
            y = boundaryBot * blockSize - (nUnits - 1) * blockSize;
        }
        this.setTranslationY(y);

    }

    @Override
    protected void touchUp(MotionEvent motionEvent) {
        float adjustmentY = Math.round(y /blockSize) *blockSize;
        this.setTranslationY(adjustmentY);
    }

}
