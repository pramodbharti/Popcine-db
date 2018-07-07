package com.example.pramod.popcine.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PopcineSplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                startActivity(new Intent(PopcineSplashActivity.this,PopcineMainActivity.class));
                finish();
    }
}
