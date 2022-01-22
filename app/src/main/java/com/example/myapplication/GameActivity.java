package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("fragment_level");

        if (fragment == null){
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new LevelFragment();
            ft.add(android.R.id.content, fragment, "fragment_level");
            ft.commit();
        }

    }
}