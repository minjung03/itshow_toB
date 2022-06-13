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

class MainCustomAdapter (
        private val context: Context, private val dataList: ArrayList<MainData>
): RecyclerView.Adapter<MainCustomAdapter.ItemViewHolder>() {

    var mPosition = 0

    inner class ItemViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView){
        val item_Title = itemView.findViewById<TextView>(R.id.item_Title)
        val item_minPrice = itemView.findViewById<TextView>(R.id.item_minPrice)
        val item_textDetail = itemView.findViewById<TextView>(R.id.item_textDetail)
        val item_locationUser = itemView.findViewById<TextView>(R.id.item_locationUser)
        val item_heartUser = itemView.findViewById<TextView>(R.id.item_heartUser)
        val item_date = itemView.findViewById<TextView>(R.id.item_date)

        fun bind(maindata:MainData, context:Context){
            item_Title.text = maindata.title;
            item_minPrice.text = maindata.price.toString()
            item_textDetail.text = maindata.text
            item_locationUser.text = maindata.location
            item_heartUser.text = maindata.heart.toString()
            item_date.text = maindata.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

}
