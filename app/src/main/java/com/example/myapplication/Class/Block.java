package com.example.myapplication.Class;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;

public abstract class Block extends androidx.appcompat.widget.AppCompatImageView {
    int blockSize;
    int nUnits = 0;
    protected int blockId;
    LevelPresenter levelPresenter;
    public Block(Context context, int blockSize) {
        super(context);
        this.blockSize = blockSize;
        this.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.block, null));
        this.setPadding(4,4,4,4);
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
    void setBlockId(int id){
        this.blockId = id;
    }
    public void setLevelPresenter(LevelPresenter levelPresenter) {
        this.levelPresenter = levelPresenter;
    }
    int getBlockId(){
        return this.blockId;
    }

    void addToMoves(int blockId, int pos){
        this.levelPresenter.addToMoves(blockId, pos);
    }

    protected abstract void touchDown(MotionEvent motionEvent);
    protected abstract void touchMove(MotionEvent motionEvent);
    protected abstract void touchUp(MotionEvent motionEvent);
    protected abstract void undoMove(int pos);
}


