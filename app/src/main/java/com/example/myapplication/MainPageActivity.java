package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

    }
    private static final String TAG = "MainPageActivity";

    public void play(View view) {
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    public void about(View view) {
        Log.v(TAG, "about");
    }

    public void exit(View view) {
        Log.v(TAG, "exit");
        this.finish();
    }
}