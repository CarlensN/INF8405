package com.example.myapplication.Class;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class BlockV extends Block{
    int y = 0;
    public BlockV(Context context, int nUnits) {
        super(context);
        this.y = y;
        this.setLayoutParams(new RelativeLayout.LayoutParams(BLOCK_SIZE, nUnits * BLOCK_SIZE));
    }

    @Override
    protected void touchDown(MotionEvent motionEvent) {
        
    }

    @Override
    public void touchMove(MotionEvent motionEvent) {
        y += motionEvent.getY();
        this.setTranslationY(y);
    }
}
