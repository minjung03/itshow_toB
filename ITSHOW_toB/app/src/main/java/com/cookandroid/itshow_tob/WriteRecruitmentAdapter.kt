package com.cookandroid.itshow_tob

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class WriteRecruitmentAdapter(val img: Bitmap?): RecyclerView.Adapter<WriteRecruitmentAdapter.CustomViewHolder>(){

    public var imgPath = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.write_recruitment_image_item, parent, false)
        return CustomViewHolder(view).apply {
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.img.setImageBitmap(img)
        imgPath = holder.img.resources.toString()
    }


    inner class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val img = itemView.findViewById<ImageView>(R.id.img_writeRecruitment)
    }

}