package com.example.myapplication.Class;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;

import java.util.ArrayList;

public abstract class Block extends androidx.appcompat.widget.AppCompatImageView {
    int blockSize;
    int nUnits = 0;
    LevelMediator levelMediator;
    @SuppressLint("ClickableViewAccessibility")
    public Block(Context context, int blockSize) {
        super(context);
        this.blockSize = blockSize;
        this.setImageDrawable(getResources().getDrawable(R.drawable.block));
        this.setOnTouchListener(new OnTouchBlockListener());
    }
    public class OnTouchBlockListener implements OnTouchListener {
        public static final String DEBUG_TAG = "ON_TOUCH_BLOCK" ;
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int action = motionEvent.getActionMasked();
            switch (action) {
                case (MotionEvent.ACTION_DOWN):
                    touchDown(motionEvent);
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    touchMove(motionEvent);
                    return true;
                case (MotionEvent.ACTION_UP):
                    touchUp(motionEvent);
                    return true;
                case (MotionEvent.ACTION_CANCEL):
                    Log.d(DEBUG_TAG, "Action was CANCEL");
                    return true;
                case (MotionEvent.ACTION_OUTSIDE):
                    Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                            "of current screen element");
                    return true;
                default:
                    return Block.super.onTouchEvent(motionEvent);
            }
        }
    }

    public void setLevelMediator(LevelMediator levelMediator) {
        this.levelMediator = levelMediator;
    }

    protected abstract void touchDown(MotionEvent motionEvent);
    protected abstract void touchMove(MotionEvent motionEvent);
    protected abstract void touchUp(MotionEvent motionEvent);

}


