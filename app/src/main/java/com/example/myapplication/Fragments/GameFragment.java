package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Class.LevelView;
import com.example.myapplication.R;

import java.io.IOException;

public class GameFragment extends Fragment {
    LevelView levelView = null;

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

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        levelView = view.findViewById(R.id.levelContainer);
        int blockSize = getActivity().getWindow().getDecorView().getWidth() / 8;
        levelView.setBlockSize(blockSize);
        try {
           levelView.updateLevel(R.raw.level1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}