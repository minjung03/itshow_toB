package com.cookandroid.itshow_tob

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

var curFragmentName = ""
class FrameMain : AppCompatActivity() {

    var savedInstanceState: Bundle? = null
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_main)
        this.savedInstanceState = savedInstanceState
        //프래그먼트들
        val fragmentSearch = Search()
        val fragmentMain= MainFragment()
        val fragmentUserInfo= UserInfoFragment()
        val fragmentHearList = HeartList()
        val fragmentHearChat = ChatListFragment()

        //프래그먼트를 적용
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragmentMain)
        transaction.commit()

        val btn_home = findViewById<ImageButton>(R.id.btn_home)
        val btn_dibs = findViewById<ImageButton>(R.id.btn_dibs)
        val btn_cheat_list = findViewById<ImageButton>(R.id.btn_cheat_list)
        val btn_user_info = findViewById<ImageButton>(R.id.btn_user_info)
        val btn_write = findViewById<ImageButton>(R.id.btn_write)

        Log.d(TAG, curFragmentName)

        btn_home.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentMain)
            transaction.commit()
            curFragmentName = "home"
        /*    var intent = Intent(this, FrameMain::class.java);
            finish()
            startActivity(intent)*/

        }
        btn_dibs.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentHearList)
            transaction.commit()
            curFragmentName = "dibs"
        }
        btn_cheat_list.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentHearChat)
            transaction.commit()
            curFragmentName = "cheat"
        }
        //내 정보
        btn_user_info.setOnClickListener{
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragmentUserInfo)

            transaction.commit()
            curFragmentName = "info"
        }
        btn_write.setOnClickListener{
            val intent = Intent(this, WriteRecruitment::class.java)
            startActivity(intent)
            curFragmentName = "home"
        }

        when(curFragmentName){
            "home"-> btn_home.performClick()
            "dibs"-> btn_dibs.performClick()
            "cheat"-> btn_cheat_list.performClick()
            "info"-> btn_user_info.performClick()
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}


