package com.example.myapplication.Class;

import android.content.Context;
import android.util.Log;
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
    protected void touchDown(MotionEvent motionEvent) {
        this.offsetY = motionEvent.getY();
        this.y = this.getTranslationY();
        this.findBoundaries();

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
        this.y = adjustmentY;
        int[][] map = levelPresenter.getMap();
        int x = (int) this.getTranslationX() / blockSize;

        for (int i = 1; i < map.length; i++){
            if (map[x][i] == getBlockId()){
                map[x][i] = - 1;
            }
        }
        int pointY = (int) (this.getTranslationY() / blockSize);
        for (int i = 0; i < nUnits; i++){
            levelPresenter.getMap()[x][pointY + i] = getBlockId();
        }
    }

    public void findBoundaries() {
        int x = (int) this.getTranslationX()/this.blockSize;
        int y = (int) this.getTranslationY()/this.blockSize;
        int closestTop = 0;
        int closestBot = 7;
        int [][] map = levelPresenter.getMap();
        for(int i = 1 ; i< map.length; i++){
            int point = map[x][i];
            if(point != this.getBlockId() && point != -1){
                if(i < y && i > closestTop ){
                    closestTop = i;
                }
                else if (i > y && i < closestBot){
                    closestBot = i;
                }
            }

        }
        boundaryTop = closestTop + 1;
        boundaryBot = closestBot - 1;
    }

}
