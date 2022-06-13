package com.cookandroid.itshow_tob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter (
    private val context: Context, private val dataList: ArrayList<RecruitmentData>
): RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

    var mPosition = 0

    inner class ItemViewHolder(itemView: View):
     RecyclerView.ViewHolder(itemView){
        private val Title = itemView.findViewById<TextView>(R.id.item_Title);
        private val minPrice = itemView.findViewById<TextView>(R.id.item_minPrice);
        private val textDetail = itemView.findViewById<TextView>(R.id.item_textDetail);
        private val Location = itemView.findViewById<TextView>(R.id.item_locationUser);
        private val Heart = itemView.findViewById<TextView>(R.id.item_heartUser);
        private val Date = itemView.findViewById<TextView>(R.id.item_date);

        fun bind(Rdata:RecruitmentData, context:Context){
            Title.text = Rdata.Title;
            minPrice.text = Rdata.minPrice.toString()
            textDetail.text = Rdata.DetailsText
            Location.text = Rdata.Location
            Heart.text = Rdata.heartNum.toString()
            Date.text = Rdata.Date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}