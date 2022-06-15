package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.intellij.lang.annotations.JdkConstants

class UserCustomAdapter (
    private val context: Context, private val dataList: ArrayList<UserInfoData>
): RecyclerView.Adapter<UserCustomAdapter.ViewHolder>() {

    var mPosition = 0

    inner class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        val item_Title = itemView.findViewById<TextView>(R.id.item_Title)
        val item_minPrice = itemView.findViewById<TextView>(R.id.item_minPrice)
        val item_textDetail = itemView.findViewById<TextView>(R.id.item_textDetail)
        val item_locationUser = itemView.findViewById<TextView>(R.id.item_locationUser)
        val item_heartUser = itemView.findViewById<TextView>(R.id.item_heartUser)
        val item_date = itemView.findViewById<TextView>(R.id.item_date)

        fun bind(userdata:UserInfoData, context:Context){
            item_Title.text = userdata.title;
            item_minPrice.text = userdata.price.toString()
            item_textDetail.text = userdata.text
            item_locationUser.text = userdata.location
            item_heartUser.text = userdata.heart.toString()
            item_date.text = userdata.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.userinfo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], context)
        // val intent = Intent(context, DetailsRecruitment::class.java)
        // context.startActivity(intent)
    }

}

