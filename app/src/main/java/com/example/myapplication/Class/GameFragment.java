package com.example.myapplication.Class;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

import java.io.IOException;

public class GameFragment extends Fragment {
    LevelContainer levelContainer = null;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        /*levelContainer = view.findViewById(R.id.levelContainer);
        initMediator();*/
        return view;
    }

    public void initMediator(){
        LevelMediator levelMediator = new LevelMediator();
        levelMediator.setLevelContainer(levelContainer);
        levelContainer.setLevelMediator(levelMediator);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        levelContainer = view.findViewById(R.id.levelContainer);
        initMediator();
        int blockSize = getActivity().getWindow().getDecorView().getWidth() / 8;
        levelContainer.setBlockSize(blockSize);
        try {
            levelContainer.generateLevel(R.raw.level1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("something", "onViewCreated");
    }
}