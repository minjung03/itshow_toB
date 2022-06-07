package com.cookandroid.itshow_tob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.itshow_tob.databinding.SearchBinding

class ReviewAdapter(val reviewItemList: ArrayList<ReviewItem>):RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ReviewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener{
                val curPos : Int = adapterPosition //누른 뷰의 순서값
                val reviewItem : ReviewItem = reviewItemList.get(curPos)
                Toast.makeText(parent.context, "리뷰입니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBindViewHolder(holder: ReviewAdapter.CustomViewHolder, position: Int) {
        holder.imageProfile.setImageResource(reviewItemList.get(position).img)
        holder.textName.setText(reviewItemList.get(position).name)
    }

    override fun getItemCount(): Int {
        return reviewItemList.size
    }

    inner class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val imageProfile = itemView.findViewById<ImageView>(R.id.image_profile_review)
        val textName = itemView.findViewById<TextView>(R.id.text_name_review)

    }
}