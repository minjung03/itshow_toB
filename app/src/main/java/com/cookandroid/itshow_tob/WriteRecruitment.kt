package com.cookandroid.itshow_tob

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class WriteRecruitment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_recruitment)

        val img_SettingBack = findViewById<ImageView>(R.id.img_SettingBack)

        // 뒤로가기 버튼 (내 프로필 화면으로 이동)
        img_SettingBack.setOnClickListener {
            val intent = Intent(applicationContext, Search::class.java)
            startActivity(intent)
        }

    }
}
