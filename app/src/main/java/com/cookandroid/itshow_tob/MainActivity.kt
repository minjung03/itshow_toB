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
        setContentView(R.layout.search)

//        Handler().postDelayed(Runnable() {
//            kotlin.run {
//                val login_intent = Intent(this, Search::class.java)
//                startActivity(login_intent)
//            }
//        }, 2000); // 2초 뒤 자동 화면 전환

    }
}
