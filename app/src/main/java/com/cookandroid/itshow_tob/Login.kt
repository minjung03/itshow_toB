package com.cookandroid.itshow_tob

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {



    private var backButtonTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

         var Id_Login_Edit: EditText
         var Pass_Login_Edit: EditText
         var AutoLogin_Check: CheckBox
         var btn_Login: Button
         var Text_Move_Join: TextView

        Id_Login_Edit = findViewById(R.id.Id_Login_Edit)
        Pass_Login_Edit = findViewById(R.id.Pass_Login_Edit)
        AutoLogin_Check = findViewById(R.id.AutoLogin_Check)
        btn_Login = findViewById(R.id.btn_Login)
        Text_Move_Join = findViewById(R.id.Text_Move_Join)

        // 회원가입 화면으로 전환
        Text_Move_Join.setOnClickListener {
            val join_intent = Intent(applicationContext, Join::class.java)
            startActivity(join_intent)
        }

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backButtonTime + 2000) { // 2초 안에 back 버튼을 다시누르면 종료
            backButtonTime = System.currentTimeMillis() // 현재 시각이 저장 된 시각에 2초를 더한 것 보다 크면 backButtonTime을 현재 시각으로 초기화
            Toast.makeText(this, "버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() <= backButtonTime + 2000) { // 현재 시각이 저장 된 시각에 2초를 더한 것 보다 작으면 어플 종료
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}
