package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.Fragments.GameFragment;
import com.example.myapplication.Fragments.HomeFragment;

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

    public void play(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GameFragment())
                .addToBackStack(GameFragment.class.getSimpleName())
                .commit();
    }

    public void exit(View view) {
        this.finish();
    }
}