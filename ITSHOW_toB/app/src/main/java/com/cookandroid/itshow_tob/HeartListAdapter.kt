package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import org.intellij.lang.annotations.JdkConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HeartListAdapter (
        private val context: Context, private val dataList: ArrayList<MainData>
): RecyclerView.Adapter<HeartListAdapter.ItemViewHolder>() {

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
            item_date.text = maindata.date

            var r_no = maindata.r_no

            val apiService2 = retrofit.create(Post::class.java)
            val apiCallForData2 = apiService2.post_like(r_no)

            apiCallForData2.enqueue(object :Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val data = response.body()
                    item_heartUser.text = data.toString().substring(5, data.toString().length - 2)
                }
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.d("실패",t.toString())
                }
            })
            itemView.setOnClickListener{
                Intent(context, DetailsRecruitment::class.java).apply {
                    putExtra("r_no", r_no)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.heart_list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

}
