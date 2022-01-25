package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.io.IOException;


import com.example.myapplication.Class.LevelContainer;
import com.example.myapplication.Class.LevelMediator;

public class GameActivity extends AppCompatActivity {
    LevelContainer levelContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        levelContainer = findViewById(R.id.levelContainer);
        initMediator();

    }
    public void initMediator(){
        LevelMediator levelMediator = new LevelMediator();
        levelMediator.setLevelContainer(levelContainer);
        levelContainer.setLevelMediator(levelMediator);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        int blockSize = levelContainer.getWidth() / 8;
        levelContainer.setBlockSize(blockSize);
        try {
            levelContainer.generateLevel(R.raw.level1);
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
