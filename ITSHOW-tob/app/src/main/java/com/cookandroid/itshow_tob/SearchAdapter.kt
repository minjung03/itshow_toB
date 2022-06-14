package com.cookandroid.itshow_tob

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.itshow_tob.databinding.SearchBinding
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(val searchWordList: ArrayList<SearchWord>):RecyclerView.Adapter<SearchAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SearchAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return CustomViewHolder(view).apply {
            view.item_button.setOnClickListener{
                val curPos : Int = adapterPosition //누른 뷰의 순서값
                val searchWord : SearchWord = searchWordList.get(curPos)
                Toast.makeText(parent.context, "키워드 : ${searchWord.word}", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onBindViewHolder(holder: SearchAdapter.CustomViewHolder, position: Int) {
        holder.word.text = searchWordList.get(position).word
    }

    override fun getItemCount(): Int {
        return searchWordList.size
    }

    inner class CustomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val word = itemView.findViewById<Button>(R.id.item_button)
    }
}