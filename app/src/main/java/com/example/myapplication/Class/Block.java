package com.example.myapplication.Class;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;

public abstract class Block extends androidx.appcompat.widget.AppCompatImageView {
    public int BLOCK_SIZE = 100;
    @SuppressLint("ClickableViewAccessibility")
    public Block(Context context) {
        super(context);
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
                    Log.d(DEBUG_TAG, "Action was DOWN");
                    touchDown(motionEvent);
                    return true;
                case (MotionEvent.ACTION_MOVE):
                    touchMove(motionEvent);
                    return true;
                case (MotionEvent.ACTION_UP):
                    Log.d(DEBUG_TAG, "Action was UP");
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

    protected abstract void touchDown(MotionEvent motionEvent);
    protected abstract void touchMove(MotionEvent motionEvent);


}


