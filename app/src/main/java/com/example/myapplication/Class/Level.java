package com.example.myapplication.Class;

import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Level {

    private int currentLevel;
    private int minMoves;
    private int record;
    private int[][] map;
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
            blocksInfo = new ArrayList<>();
            map = new int[8][8];
            for (int[] ints : map) {
                Arrays.fill(ints, -1);
            }
            int counter = 0;
            for (; ;) {
                counter++;
                line = br.readLine();
                if (line == null)
                    break;
                String[] blockInfo = line.split(" ");
                char type = blockInfo[0].charAt(0);
                for(int i = 1; i < blockInfo.length; i++) {
                    int x = Character.getNumericValue(blockInfo[i].charAt(0));
                    int y = Character.getNumericValue(blockInfo[i].charAt(2));
                    if(i == 1){
                        int nUnits = blockInfo.length - 1;
                        blocksInfo.add(new BlockInfo(counter,type, x, y, nUnits));
                    }
                    map[x][y] = counter;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BlockInfo> getBlocksInfo() {
        return blocksInfo;
    }

    public int[][] getMap() {
        return map;
    }


}
