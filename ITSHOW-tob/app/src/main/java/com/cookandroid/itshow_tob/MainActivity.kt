package com.cookandroid.itshow_tob

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler().postDelayed(Runnable() {
            run() {
                val login_intent = Intent(this@MainActivity, Main::class.java)
                startActivity(login_intent);
            }
        }, 1500); // 1.5초 뒤 자동 화면 전환

    }
}
