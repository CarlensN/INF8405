package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Class.GameFragment;
import com.example.myapplication.Class.HomeFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .addToBackStack(HomeFragment.class.getSimpleName())
                .commit();
    }
    private static final String TAG = "MainPageActivity";

    public void play(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GameFragment())
                .addToBackStack(GameFragment.class.getSimpleName())
                .commit();
    }

    public void exit(View view) {
        Log.v(TAG, "exit");
        this.finish();
    }
}