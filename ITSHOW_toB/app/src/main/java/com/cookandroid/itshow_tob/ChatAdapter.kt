package com.cookandroid.itshow_tob

import android.content.ContentValues.TAG
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatAdapter(val currentUser1: String, val itemList: ArrayList<ChatData>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    val db = Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


//        db.collection("Chat")
//            .whereEqualTo("room", "1")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d(TAG, "----------------${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "----------Error getting documents.", exception)
//            }



        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (currentUser1 == itemList[position].nickname) {

            holder.laychat.gravity = Gravity.RIGHT
            holder.laywidget.setBackgroundResource(R.drawable.chat_style_my)
        }

        holder.nickname.text = itemList[position].nickname
        holder.contents.text = itemList[position].contents
        holder.time.text = itemList[position].time
    }

/*
    private fun getMessageList() {
        FirebaseAuth.getInstance().getReference().child("Chat").child(chatRoomUid)
            .child("comments").addValueEventListener(object : ValueEventListener() {
                fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    for (dataSnapshot in snapshot.getChildren()) {
                        comments.add(dataSnapshot.getValue(ChatModel.Comment::class.java))
                    }
                    notifyDataSetChanged()
                    recyclerView.scrollToPosition(comments.size() - 1)
                }

                fun onCancelled(error: DatabaseError) {}
            })
    }
*/

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val laychat: LinearLayout = itemView.findViewById(R.id.layout_chat)
        val laywidget: LinearLayout = itemView.findViewById(R.id.layout_widget)
        val nickname: TextView = itemView.findViewById(R.id.chat_tv_nickname)
        val contents: TextView = itemView.findViewById(R.id.chat_tv_contents)
        val time: TextView = itemView.findViewById(R.id.chat_tv_time)



    }
}