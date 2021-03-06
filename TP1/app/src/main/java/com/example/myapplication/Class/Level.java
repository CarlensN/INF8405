package com.example.myapplication.Class;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Level {
    private Stack<Pair<Integer, Integer>> movesStack;
    private int currentLevel;
    private int minMoves;
    private int[][] map;
    private ArrayList<BlockInfo> blocksInfo;

    public Level(){
        movesStack = new Stack<>();
    }
    public int getCurrentLevel() {
        return currentLevel;
    }
    public int getMinMoves(){ return minMoves; }
    public void updateLevel(int id, InputStream is) {
        currentLevel = id;
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

    public void addToMoves(int id, int pos){
        this.movesStack.push(new Pair<>(id,pos));
    }

    public Stack<Pair<Integer, Integer>> getMovesStack() {
        return movesStack;
    }

    public int[][] getMap() {
        return map;
    }
}
