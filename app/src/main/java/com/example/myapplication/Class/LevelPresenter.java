package com.example.myapplication.Class;
import static java.lang.Thread.sleep;

import android.content.DialogInterface;
import android.os.Handler;
import android.util.Pair;
import android.widget.PopupMenu;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.GameFragment;
import com.example.myapplication.Fragments.SuccessFragment;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.logging.LogRecord;

public class LevelPresenter {
    final Handler handler = new Handler();
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

    public int getCurrentLevel(){
        return level.getCurrentLevel();
    }

    public void showWinDialog(){
        SuccessFragment dialog = new SuccessFragment(this);
        dialog.show(gameFragment.getActivity().getSupportFragmentManager(), "fragment_success");
        dialog.setCancelable(false);
        Runnable runnable = () -> {
            dialog.dismiss();
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onNextLevel();
        };
        handler.postDelayed(runnable, 3000);
    }

    public void removeListeners(){
        levelView.removeListeners();
        gameFragment.setUndoVisibility(false);
    }

}
