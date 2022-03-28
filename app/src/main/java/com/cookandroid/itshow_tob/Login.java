package com.cookandroid.itshow_tob;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText Id_Login_Edit, Pass_Login_Edit;
    CheckBox AutoLogin_Check;
    Button btn_Login;
    TextView Text_Move_Join;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Id_Login_Edit = findViewById(R.id.Id_Login_Edit);
        Pass_Login_Edit = findViewById(R.id.Pass_Login_Edit);
        AutoLogin_Check = findViewById(R.id.AutoLogin_Check);
        btn_Login = findViewById(R.id.btn_Login);
        Text_Move_Join = findViewById(R.id.Text_Move_Join);

        // 회원가입 화면으로 전환
        Text_Move_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent join_intent = new Intent(getApplicationContext(), Join.class);
                startActivity(join_intent);
            }
        });

    }
}
