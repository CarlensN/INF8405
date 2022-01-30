package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Class.LevelView;
import com.example.myapplication.R;

import java.io.IOException;

public class GameFragment extends Fragment {
    int currentLevel;
    int minMoves;
    LevelView levelView = null;
    TextView puzzleNumber = null;
    TextView recordCounter = null;
    TextView minimumMoves = null;
    Button nextButton = null;
    Button prevButton = null;
    Button pauseButton = null;
    Button undoButton = null;
    Button resetButton = null;

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
        puzzleNumber = view.findViewById(R.id.puzzle_number);
        recordCounter = view.findViewById(R.id.record_counter);
        minimumMoves = view.findViewById(R.id.minimumMoves);
        nextButton = view.findViewById(R.id.next_button);
        prevButton = view.findViewById(R.id.previous_button);
        pauseButton = view.findViewById(R.id.pause_button);
        undoButton = view.findViewById(R.id.undoButton);
        resetButton = view.findViewById(R.id.resetButton);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        levelView = view.findViewById(R.id.levelContainer);
        int blockSize = getActivity().getWindow().getDecorView().getWidth() / 8;
        levelView.setBlockSize(blockSize);
        try {
           levelView.updateLevel(R.raw.level1);
           currentLevel = levelView.levelPresenter.level.getCurrentLevel();
           minMoves = levelView.levelPresenter.level.getMinMoves();

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateView();
        setPreviousNextVisibility();
        setListeners();
    }

    private void setListeners() {
        nextButton.setOnClickListener(view -> {
            try {
                next();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        prevButton.setOnClickListener(view -> {
            try {
                prev();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pauseButton.setOnClickListener(view -> pause());

        undoButton.setOnClickListener(view -> undo());

        resetButton.setOnClickListener(view -> {
            try {
                reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateView() {
        puzzleNumber.setText(String.valueOf(levelView.levelPresenter.level.getCurrentLevel()));
        minimumMoves.setText(String.valueOf(levelView.levelPresenter.level.getMinMoves()));
    }


    public void next() throws IOException {
        switch(currentLevel){
            case 1:
                levelView.updateLevel(R.raw.level2);
                break;

            case 2:
                levelView.updateLevel(R.raw.level3);
                break;
        }
        currentLevel = levelView.levelPresenter.level.getCurrentLevel();
        updateView();
        setPreviousNextVisibility();
    }

    public void prev() throws IOException {
        switch(currentLevel){
            case 2:
                levelView.updateLevel(R.raw.level1);
                break;

            case 3:
                levelView.updateLevel(R.raw.level2);
                break;
        }
        currentLevel = levelView.levelPresenter.level.getCurrentLevel();
        updateView();
        setPreviousNextVisibility();
    }

    public void setPreviousNextVisibility(){
        switch (currentLevel){
            case 1 :
                prevButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                break;
            case 2:
                prevButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                break;

            case 3:
                prevButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
                break;
        }
    }

    public void pause(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void undo(){

    }

    public void reset() throws IOException {
        switch (currentLevel){
            case 1:
                levelView.updateLevel(R.raw.level1);
                break;
            case 2:
                levelView.updateLevel(R.raw.level2);
                break;

            case 3:
                levelView.updateLevel(R.raw.level3);
                break;
        }
    }
}