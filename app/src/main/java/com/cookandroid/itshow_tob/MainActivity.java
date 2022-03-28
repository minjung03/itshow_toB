package com.cookandroid.itshow_tob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.btnTest);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다~!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });
        Toast.makeText(getApplicationContext(), "안녕하세요~~", Toast.LENGTH_SHORT).show();
    }
}
