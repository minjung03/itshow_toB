package com.cookandroid.itshow_tob

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.UserInfo
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext


class FollowAdapter(val followDataList: ArrayList<FollowListData>):RecyclerView.Adapter<FollowAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FollowAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_list_item, parent, false)

        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPos : Int = adapterPosition //누른 뷰의 순서값
                val followData : FollowListData = followDataList.get(curPos)

                if(!followData.name.equals(USER_NAME)) {
                    //그 사람의 프로필로 이동하는 코드
                    val intent = Intent(parent.context, UserInfoActicity::class.java)
                    intent.putExtra("u_name", followData.name)
                    intent.putExtra("preFollow", preFollowWithFollow)
                    intent.putExtra("preEmail", preEmailWithFollow)
                    parent.context.startActivity(intent)
                    (parent.context as Activity).finish() //액티비티를 종료
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FollowAdapter.CustomViewHolder, position: Int) {
        holder.name.text = followDataList.get(position).name
        //url로 이미지 로드
        Glide.with(holder.itemView).load(followDataList.get(position).img).into(holder.img)
    }

    override fun getItemCount(): Int {
        return followDataList.size
    }

    inner class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val linearlayout_follow = itemView.findViewById<LinearLayout>(R.id.linearlayout_follow)
        val name = itemView.findViewById<TextView>(R.id.text_userName)
        val img = itemView.findViewById<ImageView>(R.id.img_userProfile)
        //Glide.with(this).load(photoUrl).into(user_image) --> 이미지 url 가져와서 user_image에 세팅
    }
}