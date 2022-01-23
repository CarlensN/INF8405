package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;


import com.example.myapplication.Class.Block;
import com.example.myapplication.Class.BlockH;
import com.example.myapplication.Class.BlockV;

public class GameActivity extends AppCompatActivity {
    static final char BLOCK_H = 'h';
    static final char BLOCK_V = 'v';
    RelativeLayout gameContainer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameContainer = findViewById((R.id.gameContainer));
        createBlock(BLOCK_H,500, 1000);
        createBlock(BLOCK_V,0,200);

    }

    public void createBlock(char type, int x, int y){
        Block block = type == BLOCK_H ? new BlockH(this,3) : new BlockV(this, 4);
        gameContainer.addView(block);
        block.setTranslationX(x);
        block.setTranslationY(y);
    }


}