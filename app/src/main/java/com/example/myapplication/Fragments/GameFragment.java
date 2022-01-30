package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Class.LevelPresenter;
import com.example.myapplication.Class.LevelView;
import com.example.myapplication.R;

public class GameFragment extends Fragment {
    LevelPresenter levelPresenter = null;
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
        levelPresenter = new LevelPresenter(this, levelView);
        levelView.setLevelPresenter(levelPresenter);
        int blockSize = getActivity().getWindow().getDecorView().getWidth() / 8;
        levelView.setBlockSize(blockSize);
        levelView.displayMap(getContext());
        levelPresenter.updateLevel(1);
        setListeners();
    }

    private void setListeners() {
        nextButton.setOnClickListener(view ->
                levelPresenter.onNextLevel()
        );

        prevButton.setOnClickListener(view ->
            levelPresenter.onPrevLevel()
        );
    }

    public void updateTopBarDisplay(int levelNumber, int minMoves) {
        puzzleNumber.setText(String.valueOf(levelNumber));
        minimumMoves.setText(String.valueOf(minMoves));
    }

    public void setPreviousVisibility(boolean isVisible){
        if(isVisible){
            prevButton.setVisibility(View.VISIBLE);
        }
        else{
            prevButton.setVisibility(View.GONE);
        }
    }

    public void setNextVisibility(boolean isVisible){
        if(isVisible){
            nextButton.setVisibility(View.VISIBLE);
        }
        else{
            nextButton.setVisibility(View.GONE);
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