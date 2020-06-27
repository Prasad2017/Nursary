package com.nursery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.nursery.R;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        moveNext();

    }

    private void moveNext() {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

}