package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.intellij.lang.annotations.JdkConstants

class UserCustomAdapter (
    private val context: Context, private val dataList: ArrayList<UserInfoData>
): RecyclerView.Adapter<UserCustomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        val item_Title = itemView.findViewById<TextView>(R.id.item_Title)
        val item_minPrice = itemView.findViewById<TextView>(R.id.item_minPrice)
        val item_textDetail = itemView.findViewById<TextView>(R.id.item_textDetail)
        val item_locationUser = itemView.findViewById<TextView>(R.id.item_locationUser)
        val item_heartUser = itemView.findViewById<TextView>(R.id.item_heartUser)
        val item_date = itemView.findViewById<TextView>(R.id.item_date)

        val image_minPrice = itemView.findViewById<ImageView>(R.id.image_minPrice)
        val image_heart = itemView.findViewById<ImageView>(R.id.image_heart)
        val image_location = itemView.findViewById<ImageView>(R.id.image_location)


        fun bind(userdata:UserInfoData, context:Context){
            val date = userdata.date;
            if(date.substring(1, date.length-1).toInt() <= 0) {
                item_Title.text = userdata.title
                item_minPrice.text = userdata.price
                item_textDetail.text = userdata.text
                item_locationUser.text = userdata.location
                item_heartUser.text = userdata.heart.toString()
                item_date.text = userdata.date

                image_minPrice.setImageResource(R.drawable.main_item_minprice_style2)
                image_heart.setImageResource(R.drawable.heart_black)
                image_location.setImageResource(R.drawable.map_icon_black)
                item_Title.setTextColor(Color.parseColor("#a0a0a0"))

                item_minPrice.setText("모집 종료")
                item_minPrice.setTextColor(Color.parseColor("#a0a0a0"))

                item_textDetail.setTextColor(Color.parseColor("#a0a0a0"))
                item_locationUser.setTextColor(Color.parseColor("#a0a0a0"))
                item_heartUser.setTextColor(Color.parseColor("#a0a0a0"))

                item_date.setText("")
                item_date.setTextColor(Color.parseColor("#a0a0a0"))

                itemView.setOnClickListener{
                    Toast.makeText(context, "모집 종료된 게시글 입니다", Toast.LENGTH_SHORT).show()
                }

            }
            else {
                item_Title.text = userdata.title;
                item_minPrice.text = userdata.price
                item_textDetail.text = userdata.text
                item_locationUser.text = userdata.location
                item_heartUser.text = userdata.heart.toString()
                item_date.text = userdata.date

                var r_no = userdata.r_no

                itemView.setOnClickListener{
                    Intent(context, DetailsRecruitment::class.java).apply {
                        putExtra("r_no", r_no)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }.run { context.startActivity(this) }
                }
            }
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

