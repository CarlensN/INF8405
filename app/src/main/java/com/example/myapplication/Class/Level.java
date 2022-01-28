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
    public void updateLevel(InputStream is) {

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            line = br.readLine();
            currentLevel = Integer.parseInt(line);
            line = br.readLine();
            minMoves = Integer.parseInt(line);
            blocksInfo = new ArrayList();
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
                    if(x-1 == 0 ){
                        int nUnits = blockInfo.length - 1;
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

    public Pair<Integer,Integer> findBoundariesX(BlockH blockH) {
        int x = (int) blockH.getTranslationX()/blockH.blockSize;
        int y = (int) blockH.getTranslationY()/blockH.blockSize;
        int closestLeft = 0;
        int closestRight = 7;
        for(int i = 1 ; i<map.length; i++){
            int point = map[i][y];
            if(point != blockH.getBlockId() && point != -1){
                if(i < x && i> closestLeft ){
                    closestLeft = i;
                }
                else if (i > x && i < closestRight){
                    closestRight = i;
                }
            }

        }
        Log.i("closest left", Integer.toString(closestLeft));
        Log.i("closest right", Integer.toString(closestRight));
        return new Pair(closestLeft+1, closestRight-1);

    }

    public Pair<Integer,Integer> findBoundariesY(BlockV blockV) {
        int x = (int) blockV.getTranslationX()/blockV.blockSize;
        int y = (int) blockV.getTranslationY()/blockV.blockSize;
        int closestTop = 0;
        int closestBot = 7;
        for(int i = 1 ; i<map.length; i++){
            int point = map[x][i];
            if(point != blockV.getBlockId() && point != -1){
                if(i < y && i > closestTop ){
                    closestTop = i;
                }
                else if (i > y && i < closestBot){
                    closestBot = i;
                }
            }

        }
        Log.i("closest left", Integer.toString(closestTop));
        Log.i("closest right", Integer.toString(closestBot));
        return new Pair(closestTop+1, closestBot-1);
    }
}
