package com.example.myapplication.Class;
import com.example.myapplication.Fragments.GameFragment;
import com.example.myapplication.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

public class LevelPresenter {
    private final int MAX_LEVEL = 3;
    private LevelView levelView;
    private Level level;
    private GameFragment gameFragment;
    public LevelPresenter(GameFragment gameFragment, LevelView levelView){
        this.gameFragment = gameFragment;
        this.levelView = levelView;
        level = new Level();
        moves = new Stack<>();
    }
    public void updateLevel(int id){
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
        gameFragment.setPreviousVisibility(true);
        if(level.getCurrentLevel() < MAX_LEVEL){
            updateLevel(level.getCurrentLevel()+1);
        }
    }

    public void onPrevLevel(){
        gameFragment.setNextVisibility(true);
        if(level.getCurrentLevel() > 1){
            updateLevel(level.getCurrentLevel()-1);
        }
    }
    public int[][] getMap(){
        return level.getMap();
    }

    public void addToMoves(Pair move){
        level.getMoves().push(move);
    }
}
