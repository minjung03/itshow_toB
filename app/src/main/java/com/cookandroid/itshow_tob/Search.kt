package com.cookandroid.itshow_tob

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.itshow_tob.databinding.SearchBinding
import androidx.core.app.ComponentActivity.ExtraData
import android.os.Build.VERSION_CODES.O
import com.cookandroid.itshow_tob.Search
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.flexbox.*


class Search : AppCompatActivity() {
    private var sBinding: SearchBinding? = null
    private val binding get() = sBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        sBinding = SearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //버튼에 따른 키워드를 넣는 공간(이동될 layout(?)도)
        val searchWordList = arrayListOf(
                SearchWord("당근마켓", "carrot link"),
                SearchWord("배민", "delivery link"),
                SearchWord("요기요", "delivery link"),
                SearchWord("ㅎㅎ", "delivery link"),
                SearchWord("생활용품 공동구매", "delivery link")
        )
        val flexBoxLayoutManager = FlexboxLayoutManager(this) as FlexboxLayoutManager
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
        flexBoxLayoutManager.alignItems = AlignItems.CENTER
        flexBoxLayoutManager.justifyContent = JustifyContent.CENTER
        binding.recycelerView.layoutManager = flexBoxLayoutManager
        //binding.recycelerView.setHasFixedSize(true)
        binding.recycelerView.adapter = SearchAdapter(searchWordList)
    }
}
