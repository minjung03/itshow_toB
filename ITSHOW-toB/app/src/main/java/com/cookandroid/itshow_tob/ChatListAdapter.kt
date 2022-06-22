package com.cookandroid.itshow_tob

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatListAdapter (
    private val context: Context, private val dataList: ArrayList<ChatListData>
): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        val chat_textview_title: TextView = itemView.findViewById<TextView>(R.id.chat_textview_title)
        val chat_item_textview_lastmessage: TextView = itemView.findViewById<TextView>(R.id.chat_item_textview_lastmessage)

        fun bind(chatlistData:ChatListData, context:Context){

<<<<<<< Updated upstream
            chat_textview_title.text = chatlistData.users;
=======
            var users = chatlistData.users;
            if(users.length >= 25){
                users = chatlistData.users.substring(0,25)+"...";
            }
            chat_textview_title.text = users;
>>>>>>> Stashed changes
            chat_item_textview_lastmessage.text = chatlistData.comments
            var r_no = chatlistData.r_no

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

