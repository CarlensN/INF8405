package com.example.myapplication.Class;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class BlockV extends Block{
    float y = 0;
    float offsetY = 0;
    public BlockV(Context context,int nUnits, int blockSize) {
        super(context,blockSize);
        this.setLayoutParams(new RelativeLayout.LayoutParams(blockSize, nUnits * blockSize));
    }

    @Override
    protected void touchDown(MotionEvent motionEvent) {
        this.offsetY = motionEvent.getY();
        this.y = this.getTranslationY();
    }

    @Override
    public void touchMove(MotionEvent motionEvent) {
        y += motionEvent.getY() - this.offsetY;
        this.setTranslationY(y);
    }

    @Override
    protected void touchUp(MotionEvent motionEvent) {
        float adjustmentY = Math.round(y /blockSize) *blockSize;
        this.setTranslationY(adjustmentY);
    }
}
