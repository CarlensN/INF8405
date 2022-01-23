package com.example.myapplication.Class;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class BlockV extends Block{
    float y = 0;
    float offsetY = 0;
    public BlockV(Context context,int nUnits) {
        super(context);
        this.setLayoutParams(new RelativeLayout.LayoutParams(BLOCK_SIZE, nUnits * BLOCK_SIZE));
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
}
