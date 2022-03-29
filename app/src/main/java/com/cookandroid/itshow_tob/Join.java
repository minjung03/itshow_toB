package com.cookandroid.itshow_tob;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Join  extends AppCompatActivity {

    ImageView img_JoinBack;
    EditText Name_Join_Edit, Id_Join_Edit, Pass_Join_Edit;
    Button btn_Join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        img_JoinBack = findViewById(R.id.img_JoinBack);
        Name_Join_Edit = findViewById(R.id.Name_Join_Edit);
        Id_Join_Edit = findViewById(R.id.Id_Join_Edit);
        Pass_Join_Edit = findViewById(R.id.Pass_Join_Edit);
        btn_Join = findViewById(R.id.btn_Join);


        img_JoinBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent join_back_intent = new Intent(getApplicationContext(), Login.class);
                startActivity(join_back_intent);
            }
        });


    }
}