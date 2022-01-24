package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import com.example.myapplication.Class.Block;
import com.example.myapplication.Class.BlockH;
import com.example.myapplication.Class.BlockV;

public class GameActivity extends AppCompatActivity {

    private int _currentLevel;
    private int _minMoves;
    private int _record;
    private int blockSize;
    private List<String> levelInfo;

    @SuppressLint("ClickableViewAccessibility")
    static final char BLOCK_H = 'h';
    static final char BLOCK_V = 'v';
    RelativeLayout gameContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameContainer = findViewById(R.id.gameContainer);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        blockSize = gameContainer.getWidth() / 8;
        try {
            createLevel(R.raw.level1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void createLevel(int id) throws IOException {
        InputStream is = getResources().openRawResource(id);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<String> lines = new ArrayList();
        for(;;){
            String line = br.readLine();
            if (line == null)
                break;
            lines.add(line);
        }
        _currentLevel = Integer.parseInt(lines.get(0));
        _minMoves = Integer.parseInt(lines.get(1));

        String[] info = lines.get(2).split(" ");

        for(String line : info){
            Log.d("line", line);
        }

        for (int i = 2; i < lines.size(); i++){
            String[] blockInfo = lines.get(i).split(" ");
            int unit = blockInfo.length - 1;
            char type = blockInfo[0].charAt(0);
            int x = Character.getNumericValue(blockInfo[1].charAt(0));
            int y = Character.getNumericValue(blockInfo[1].charAt(2));
            createBlock(type, x, y, unit, blockSize);
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



    public void createBlock(char type, int x, int y, int nUnits, int blockSize){
        Block block = type == BLOCK_H ? new BlockH(this, nUnits, blockSize) : new BlockV(this, nUnits, blockSize);
        gameContainer.addView(block);
        block.setTranslationX(x*blockSize);
        block.setTranslationY(y*blockSize);
    }


}