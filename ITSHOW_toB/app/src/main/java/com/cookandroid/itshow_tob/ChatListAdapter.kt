package com.cookandroid.itshow_tob

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListAdapter (
    private val context: Context, private val dataList: ArrayList<ChatListData>
): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        val chat_textview_title: TextView = itemView.findViewById<TextView>(R.id.chat_textview_title)
        val chat_item_textview_lastmessage: TextView = itemView.findViewById<TextView>(R.id.chat_item_textview_lastmessage)

        fun bind(chatlistData:ChatListData, context:Context){

            var r_no = chatlistData.r_no
            //로딩창 객체 생성

            //투명하게
            val apiService = retrofit.create(RecruitmentAPIService::class.java)
            val apiCallForData = apiService.getRecruitmentInfo(r_no.toInt())

            apiCallForData.enqueue(object : Callback<JsonArray>{
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    Log.d("TAG", "게시글 이름 불러오기 성공")
                    val gson = Gson()
                    val recruitmentData = gson.fromJson(response.body()?.get(0).toString(), RecruitmentDatas::class.java)

                    if(chat_textview_title.text.length >= 25){
                        chat_textview_title.text = recruitmentData.r_title.substring(0,25)+"...";
                    }else{
                        chat_textview_title.text = recruitmentData.r_title
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.d("TAG", "게시글 이름 불러오기 실패")
                }
            })


            chat_item_textview_lastmessage.text = chatlistData.comments

            itemView.setOnClickListener{
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("r_no",r_no)
                Log.d("ToB", r_no)
                (context as Activity).startActivity(intent)

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chatlist, parent, false)

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

}

