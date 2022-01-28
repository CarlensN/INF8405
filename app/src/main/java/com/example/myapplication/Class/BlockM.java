package com.example.myapplication.Class;

import android.content.Context;
import android.view.MotionEvent;

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
            setTranslationX(x);
            win();
            return;
        }
        super.touchMove(motionEvent);

    }

    private void win() {
    }

    @Override
    protected void touchUp(MotionEvent motionEvent) {
        if(x + (nUnits -1) *blockSize > 6 * blockSize){
            return;
        }
        super.touchUp(motionEvent);
    }
}
