package com.example.myapplication.Class;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class BlockH extends Block {
    float offsetX = 0; //block position
    public BlockH(Context context, int nUnits) {
        super(context);
        this.setLayoutParams(new RelativeLayout.LayoutParams(nUnits * BLOCK_SIZE , BLOCK_SIZE));
    }

    @Override
    protected void touchDown(MotionEvent motionEvent) {
        this.offsetX = motionEvent.getX() - this.getTranslationX();
        Log.d("TOUCHMOVE", Float.toString(motionEvent.getX()));
        Log.d("TOUCHMOVE", Float.toString(this.getTranslationX()));
    }

    @Override
    public void touchMove(MotionEvent motionEvent) {

        this.setTranslationX(motionEvent.getX() + offsetX);
    }
}
