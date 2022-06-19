package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import org.intellij.lang.annotations.JdkConstants

class MainCustomAdapter (
        private val context: Context, private val dataList: ArrayList<MainData>
): RecyclerView.Adapter<MainCustomAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView){
        val item_Title = itemView.findViewById<TextView>(R.id.item_Title)
        val item_minPrice = itemView.findViewById<TextView>(R.id.item_minPrice)
        val item_textDetail = itemView.findViewById<TextView>(R.id.item_textDetail)
        val item_locationUser = itemView.findViewById<TextView>(R.id.item_locationUser)
        val item_heartUser = itemView.findViewById<TextView>(R.id.item_heartUser)
        val item_date = itemView.findViewById<TextView>(R.id.item_date)
        val item_recruitment = itemView.findViewById<RoundedImageView>(R.id.img_recruitment)

        fun bind(maindata:MainData, context:Context){
            item_Title.text = maindata.title;
            item_minPrice.text = maindata.price.toString()
            item_textDetail.text = maindata.text
            item_locationUser.text = maindata.location
            item_heartUser.text = maindata.heart.toString()
            item_date.text = maindata.date

            if(!maindata.imgPath.equals("")){
                item_recruitment.visibility = View.VISIBLE
                Glide.with(context).load("https://tob.emirim.kr/uploads/"+maindata.imgPath).into(item_recruitment)
            }else{
                item_recruitment.visibility = View.GONE
            }

            var r_no = maindata.r_no

            itemView.setOnClickListener{
                Intent(context, DetailsRecruitment::class.java).apply {
                    putExtra("r_no", r_no)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
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
