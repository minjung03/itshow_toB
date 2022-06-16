package com.cookandroid.itshow_tob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FollowList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.follow_recycler)


        val img_followBack = findViewById<ImageView>(R.id.img_followBack)
        val recycler_view = findViewById<RecyclerView>(R.id.recyceler_view)

        img_followBack.setOnClickListener{
            finish()
        }

        val followDataList = arrayListOf(
                FollowDataOfUserInfo("https://t1.daumcdn.net/cfile/tistory/24283C3858F778CA2E", "김다흰"),
                FollowDataOfUserInfo("http://t1.daumcdn.net/friends/prod/editor/dc8b3d02-a15a-4afa-a88b-989cf2a50476.jpg", "김다흰")
        )


        recycler_view.adapter = FollowAdapter(followDataList)
        recycler_view.layoutManager = LinearLayoutManager(this@FollowList)

    }
}