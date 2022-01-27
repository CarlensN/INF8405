package com.example.myapplication.Class;

import android.graphics.Point;
import android.renderscript.ScriptGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Level {

    private int currentLevel;
    private int minMoves;
    private int record;
    private ArrayList<BlockInfo> blocksInfo;
    public int getCurrentLevel() {
        return currentLevel;
    }
    public int getMinMoves(){ return minMoves; }
    public void updateLevel(InputStream is) {

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            line = br.readLine();
            currentLevel = Integer.parseInt(line);
            line = br.readLine();
            minMoves = Integer.parseInt(line);
            blocksInfo = new ArrayList();
            for (; ;) {
                line = br.readLine();
                if (line == null)
                    break;
                String[] blockInfo = line.split(" ");
                char type = blockInfo[0].charAt(0);
                int x = Character.getNumericValue(blockInfo[1].charAt(0));
                int y = Character.getNumericValue(blockInfo[1].charAt(2));
                int nUnits = blockInfo.length - 1;

                blocksInfo.add(new BlockInfo(type, x, y, nUnits));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BlockInfo> getBlocksInfo() {
        return blocksInfo;
    }
}
