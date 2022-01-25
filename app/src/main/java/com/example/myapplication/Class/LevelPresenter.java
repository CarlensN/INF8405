package com.example.myapplication.Class;

import android.view.View;

import java.util.ArrayList;

public class LevelPresenter {
    ArrayList<Block> blocks = new ArrayList();
    LevelView levelView;

    public LevelPresenter(LevelView view){
        this.levelView = view;
    }


}
