package com.cookandroid.itshow_tob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class HelpContent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_content)

        val img_HelpBack = findViewById<ImageView>(R.id.img_HelpBack)
        img_HelpBack.setOnClickListener{
            finish()
        }

    }
}
