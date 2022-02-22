package com.example.myapplication.Class;

import android.content.Context;

public class BlockFactory {
    static final char BLOCK_H = 'h';
    static final char BLOCK_V = 'v';
    static final char BLOCK_M = 'm';
    int blockSize;
    Context context;

    public BlockFactory(Context context){
        this.context = context;
    }
    public Block createBlock(char type, int nUnits){
        Block block = null;
        if(type == BLOCK_H){
            return new BlockH(context,nUnits, blockSize);
        }
        else if (type == BLOCK_V){
            return new BlockV(context,nUnits, blockSize);
        }
        else if(type == BLOCK_M){
            return new BlockM(context,nUnits, blockSize);
        }
        return block;

    }
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
