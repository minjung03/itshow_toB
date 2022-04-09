package com.cookandroid.itshow_tob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login_intent = new Intent(MainActivity.this, WriteRecruitment.class);
                startActivity(login_intent);
            }
        }, 2000); // 2초 뒤 자동 화면 전환
*/
    }

}
