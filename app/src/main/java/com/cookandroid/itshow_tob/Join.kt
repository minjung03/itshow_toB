package com.cookandroid.itshow_tob

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity

class Join : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

         var img_JoinBack: ImageView
         var Name_Join_Edit: EditText
         var Id_Join_Edit: EditText
         var Pass_Join_Edit: EditText
         var btn_Join: Button

        img_JoinBack = findViewById(R.id.img_JoinBack)
        Name_Join_Edit = findViewById(R.id.Name_Join_Edit)
        Id_Join_Edit = findViewById(R.id.Id_Join_Edit)
        Pass_Join_Edit = findViewById(R.id.Pass_Join_Edit)
        btn_Join = findViewById(R.id.btn_Join)


        img_JoinBack.setOnClickListener {
            val join_back_intent = Intent(applicationContext, Login::class.java)
            startActivity(join_back_intent)
        }


    }
}