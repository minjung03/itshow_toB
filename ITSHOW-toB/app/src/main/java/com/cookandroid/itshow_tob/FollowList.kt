package com.cookandroid.itshow_tob

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
var preEmailWithFollow = ""
var preFollowWithFollow = ""
class FollowList : AppCompatActivity() {
    val apiService = retrofit.create(FlowAPIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.follow_recycler)

        val img_followBack = findViewById<ImageView>(R.id.img_followBack)
        val text_followTitle = findViewById<TextView>(R.id.text_followTitle)
        val recycler_view = findViewById<RecyclerView>(R.id.recyceler_view)

        val intent = getIntent() //인텐트객체 선언
        //팔로워인지, 팔로잉목록인지와 누구꺼인지 값 가져오기
        val follow = intent.getStringExtra("follow")
        val u_email = intent.getStringExtra("u_email")

        if(u_email!=null) {
            preFollowWithFollow = follow.toString()
            preEmailWithFollow = u_email
            var followDataList:ArrayList<FollowListData> = arrayListOf()
            var apiCallForData:Call<JsonArray> = apiService.followerListOfUser(u_email)
            val loadingDialog = LoadingDialog(this@FollowList) //로딩창 객체 생성
            loadingDialog.show()
            if (follow.equals("follower")) {
                text_followTitle.text = "팔로워"
                apiCallForData = apiService.followerListOfUser(u_email)
            } else if (follow.equals("following")) {
                text_followTitle.text = "팔로잉"
                apiCallForData = apiService.flowingListOfUser(u_email)
            }
            apiCallForData.enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    loadingDialog.dismiss()
                    val data = response.body()

                    if (data != null) {
                        for (idx in 0 until data.size()){
                            val userData = Gson().fromJson(data.get(idx).toString(), UserInfoDatas::class.java)
                            followDataList.add(FollowListData(userData.u_name, userData.u_img))
                        }
                        recycler_view.adapter = FollowAdapter(followDataList)
                        recycler_view.layoutManager = LinearLayoutManager(this@FollowList)
                    }
                }
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    loadingDialog.dismiss()
                    Log.d(TAG, "실패 : "+t)
                }
            })
        }//if end

        img_followBack.setOnClickListener{
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}