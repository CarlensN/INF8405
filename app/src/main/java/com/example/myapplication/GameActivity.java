package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class GameActivity extends AppCompatActivity {

    private int _currentLevel;
    private int _minMoves;
    private int _record;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setPreviousNextVisibility(_currentLevel);
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
}