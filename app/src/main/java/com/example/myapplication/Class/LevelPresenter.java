package com.example.myapplication.Class;
import android.util.Pair;

import com.example.myapplication.Fragments.GameFragment;

import java.util.ArrayList;

public class LevelPresenter {
    private int currentRecord = 0;
    private int moveCount = 0;
    private final MovesListener moves;
    private final int MAX_LEVEL = 3;
    private final LevelView levelView;
    private final Level level;
    private final GameFragment gameFragment;
    public LevelPresenter(GameFragment gameFragment, LevelView levelView){
        this.gameFragment = gameFragment;
        this.levelView = levelView;
        level = new Level();
        moves = new MovesListener();
    }
    public void updateLevel(int id){
        clearMovesStack();
        int levelResId = gameFragment.getResources().getIdentifier(
                "level" + id,
                "raw",
                "com.example.myapplication");
        level.updateLevel(id, gameFragment.getResources().openRawResource(levelResId));
        ArrayList<BlockInfo> blocksInfo = level.getBlocksInfo();
        levelView.clearBlocks();
        levelView.placeBlocks(blocksInfo);
        gameFragment.updateTopBarDisplay(level.getCurrentLevel(),level.getMinMoves());
        if (level.getCurrentLevel() >= MAX_LEVEL){
            gameFragment.setNextVisibility(false);
        }
        if(level.getCurrentLevel() <= 1){
            gameFragment.setPreviousVisibility(false);
        }
    }

    public void onNextLevel(){
        moveCount = 0;
        moves.set(moveCount);
        gameFragment.setPreviousVisibility(true);
        if(level.getCurrentLevel() < MAX_LEVEL){
            updateLevel(level.getCurrentLevel()+1);
        }
    }

    public void onPrevLevel(){
        moveCount = 0;
        moves.set(moveCount);
        gameFragment.setNextVisibility(true);
        if(level.getCurrentLevel() > 1){
            updateLevel(level.getCurrentLevel()-1);
        }
    }
    public int[][] getMap(){
        return level.getMap();
    }

    public void addToMoves(int id, int pos) {
        moves.set(++moveCount);
        level.addToMoves(id,pos);
        this.gameFragment.setUndoVisibility(true);
    }

    public void onReset() {
        moveCount = 0;
        moves.set(moveCount);
        this.updateLevel(level.getCurrentLevel());
    }

    public void onUndo() {
        moves.set(--moveCount);
        Pair<Integer,Integer> moveToUndo = this.level.getMovesStack().pop();
        this.levelView.undoMove(moveToUndo);
        if(this.level.getMovesStack().empty()){
            this.gameFragment.setUndoVisibility(false);
        }
    }

    public void clearMovesStack(){
        this.gameFragment.setUndoVisibility(false);
        this.level.getMovesStack().clear();
    }

    public MovesListener getMovesListener(){
        return moves;
    }

    public int getCurrentRecord(){
        return currentRecord;
    }

    public void setCurrentRecord(int value){
        currentRecord = value;
    }

    public void saveCurrentRecord(int level, int currentRecord){

    }

    public int getCurrentLevel(){
        return level.getCurrentLevel();
    }

}
