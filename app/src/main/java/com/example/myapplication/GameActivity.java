package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;


import com.example.myapplication.Class.Block;
import com.example.myapplication.Class.BlockH;
import com.example.myapplication.Class.BlockV;

public class GameActivity extends AppCompatActivity {

    private int _currentLevel;
    private int _minMoves;
    private int _record;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    static final char BLOCK_H = 'h';
    static final char BLOCK_V = 'v';
    RelativeLayout gameContainer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setPreviousNextVisibility(_currentLevel);
        gameContainer = findViewById((R.id.gameContainer));
        createBlock(BLOCK_H,500, 1000);
        createBlock(BLOCK_V,0,200);

    }

    public void setFirstLevel(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new LevelFragment();
        ft.replace(android.R.id.content, fragment, "fragment_level");
        ft.addToBackStack(null);
        ft.commit();
        _currentLevel = 1;
    }

    public void createLevel(File grid){

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

    public void createBlock(char type, int x, int y){
        Block block = type == BLOCK_H ? new BlockH(this,3) : new BlockV(this, 4);
        gameContainer.addView(block);
        block.setTranslationX(x);
        block.setTranslationY(y);
    }


}