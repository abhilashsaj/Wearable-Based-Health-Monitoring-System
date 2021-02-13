package com.example.svasthya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StressHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_home);
    }

    public void five_min_meditation(View view) {
        Intent intent = new Intent(StressHomeActivity.this, FiveMinMediation.class);
        startActivity(intent);
    }

    public void goToSBreathingActivity(View view) {
        Intent intent = new Intent(StressHomeActivity.this, BreathingActivity.class);
        startActivity(intent);
    }

    public void goToWalk(View view) {
        Intent intent = new Intent(StressHomeActivity.this, Walk.class);
        startActivity(intent);
    }

    public void goToDinoGame(View view) {
        Intent intent = new Intent(StressHomeActivity.this, DinoGame.class);
        startActivity(intent);
    }
}