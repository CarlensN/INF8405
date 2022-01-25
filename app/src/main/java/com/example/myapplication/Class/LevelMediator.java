package com.example.myapplication.Class;

import java.util.ArrayList;

public class LevelMediator {
    ArrayList<Block> blocks = new ArrayList();
    LevelContainer levelContainer;

    public void setLevelContainer(LevelContainer levelContainer) {
        this.levelContainer = levelContainer;
    }

}
