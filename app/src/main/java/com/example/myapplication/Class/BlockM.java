package com.example.myapplication.Class;

import android.content.Context;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;

import com.example.myapplication.R;

public class BlockM extends BlockH {
    public BlockM(Context context, int nUnits, int blockSize) {
        super(context, nUnits, blockSize);
        this.setImageDrawable(getResources().getDrawable(R.drawable.main_block));
    }

    @Override
    public void touchMove(MotionEvent motionEvent) {
        x += motionEvent.getX() - this.offsetX;
        if(boundaryRight == 6 && x + (nUnits -1) *blockSize > 6 * blockSize ){
            win();
            return;
        }
        super.touchMove(motionEvent);

    }

    private void win() {
        this.setOnTouchListener(null);
        this.setAnimation(new TranslateAnimation(this.getX(), this.getX()+blockSize, 0, 0;
    }

    @Override
    protected void touchUp(MotionEvent motionEvent) {
        if(x + (nUnits -1) *blockSize > 6 * blockSize){
            return;
        }
        super.touchUp(motionEvent);
    }
}
