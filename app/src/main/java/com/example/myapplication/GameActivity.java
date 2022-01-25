package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.io.IOException;


import com.example.myapplication.Class.LevelView;
import com.example.myapplication.Class.LevelPresenter;

public class GameActivity extends AppCompatActivity {
    LevelView levelView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        levelView = findViewById(R.id.levelView);
        LevelPresenter levelPresenter = new LevelPresenter(levelView);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        int blockSize = levelView.getWidth() / 8;
        levelView.setBlockSize(blockSize);
        try {
            levelView.generateLevel(R.raw.level1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void previous(View view) {
    }

    public void next(View view) {
    }

    public void pause(View view) {
    }

    public void setPreviousNextVisibility(int level){
        switch (level){
            case 1 :
                findViewById(R.id.previous_button).setVisibility(View.GONE);
                findViewById(R.id.next_button).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.previous_button).setVisibility(View.VISIBLE);
                findViewById(R.id.next_button).setVisibility(View.VISIBLE);
                break;

            case 3:
                findViewById(R.id.previous_button).setVisibility(View.VISIBLE);
                findViewById(R.id.next_button).setVisibility(View.GONE);
                break;
        }
    }
}