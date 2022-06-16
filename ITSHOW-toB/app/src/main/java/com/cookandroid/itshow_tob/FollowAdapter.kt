package com.cookandroid.itshow_tob

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.scale
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.http.Url
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


class FollowAdapter(val followDataList: ArrayList<FollowDataOfUserInfo>):RecyclerView.Adapter<FollowAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FollowAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follow_list_item, parent, false)

        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPos : Int = adapterPosition //누른 뷰의 순서값
                val followData : FollowDataOfUserInfo = followDataList.get(curPos)
            }
        }
    }
    override fun onBindViewHolder(holder: FollowAdapter.CustomViewHolder, position: Int) {
        holder.name.text = followDataList.get(position).name
        Log.d(TAG, followDataList.get(position).img)

        //url로 이미지 로드
        var bitmap = getBitmapFromURL(followDataList.get(position).img)
        Log.d(TAG, bitmap?.height.toString())
        holder.img.setImageResource(R.drawable.user_nomal)
        holder.img.setImageBitmap(bitmap)
    }

    fun getBitmapFromURL(url: String?): Bitmap? {
        val imgUrl:URL
		val connection:HttpURLConnection
        val input:InputStream

		val retBitmap:Bitmap?

		try{
			imgUrl = URL(url)
			connection = imgUrl.openConnection() as HttpURLConnection
			connection.setDoInput(true) //url로 input받는 flag 허용
			connection.connect(); //연결
			input = connection.getInputStream(); // get inputstream
			retBitmap = BitmapFactory.decodeStream(input)
		}catch(e:Exception) {
            e.printStackTrace();
            return null;
        }
        if(connection != null) {
            connection.disconnect();
        }
        return retBitmap;
    }

    override fun getItemCount(): Int {
        return followDataList.size
    }

    inner class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val name = itemView.findViewById<TextView>(R.id.text_userName)
        val img = itemView.findViewById<ImageView>(R.id.img_userProfile)
        //Glide.with(this).load(photoUrl).into(user_image) --> 이미지 url 가져와서 user_image에 세팅
    }
}