package com.cookandroid.itshow_tob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class WriteRecruitment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_recruitment);

        ImageView img_SettingBack = findViewById(R.id.img_SettingBack);

        // 뒤로가기 버튼 (내 프로필 화면으로 이동)
        img_SettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

    }
}
