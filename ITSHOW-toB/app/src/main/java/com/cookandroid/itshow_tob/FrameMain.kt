package com.cookandroid.itshow_tob

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.ImageButton

class FrameMain : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_main)

        //프래그먼트들
        val fragmentSearch = Search()
        val fragmentMain= MainFragment()
        val fragmentUserInfo= UserInfo()

        //프래그먼트를 적용
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragmentMain)
        transaction.commit()

        val btn_home = findViewById<ImageButton>(R.id.btn_home)
        val btn_dibs = findViewById<ImageButton>(R.id.btn_dibs)
        val btn_cheat_list = findViewById<ImageButton>(R.id.btn_cheat_list)
        val btn_user_info = findViewById<ImageButton>(R.id.btn_user_info)
        val btn_write = findViewById<ImageButton>(R.id.btn_write)
        btn_home.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentMain)
            transaction.commit()
        }
        btn_dibs.setOnClickListener{

        }
        btn_cheat_list.setOnClickListener{

        }
        //내 정보
        btn_user_info.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentUserInfo)
            transaction.commit()
        }
        btn_write.setOnClickListener{
            val intent = Intent(this, WriteRecruitment::class.java)
            startActivity(intent)
        }

    }
}
