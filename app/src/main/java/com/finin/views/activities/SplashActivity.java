package com.finin.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.finin.utils.AppHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppHelper.setStatusBarColor(SplashActivity.this);
        startActivity(new Intent(SplashActivity.this, UserListActivity.class));
        finish();
    }
}
