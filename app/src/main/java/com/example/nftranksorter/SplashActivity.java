package com.example.nftranksorter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nftranksorter.ui.collections.CollectionListActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 2 saniye sonra CollectionListActivity'ye geç
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, CollectionListActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}