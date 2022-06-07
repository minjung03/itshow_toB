package com.cookandroid.itshow_tob

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.itshow_tob.databinding.SearchBinding
import androidx.core.app.ComponentActivity.ExtraData
import android.os.Build.VERSION_CODES.O
import com.cookandroid.itshow_tob.Search
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.cookandroid.itshow_tob.databinding.WriteRecruitmentBinding
import com.google.android.flexbox.*

class Search : Fragment() {
    private var sBinding: SearchBinding? = null
    private val binding get() = sBinding!!
    private lateinit var mainContext: FrameMain


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sBinding = SearchBinding.inflate(layoutInflater)
        val view = binding.root

        val btnButton1 = view.findViewById<Button>(R.id.btn_recentlySearch)
        val btnButton2 = view.findViewById<Button>(R.id.btn_popularitySearch)

        btnButton1.setOnClickListener{
            btnButton1.setBackgroundResource(R.drawable.button_round1_gray)
            btnButton1.setTextColor(Color.parseColor("#FFFFFF"))
            btnButton2.setBackgroundResource((R.drawable.button_round1_white))
            btnButton2.setTextColor(Color.parseColor("#000000"))
            //버튼에 따른 키워드를 넣는 공간(이동될 layout(?)도)
            val searchWordList = arrayListOf(
                    SearchWord("버튼 클릭 당근", "carrot link"),
                    SearchWord("버튼 클릭 배민", "delivery link"),
                    SearchWord("버튼 클릭 요기요", "delivery link"),
                    SearchWord("버튼 클릭 ㅎㅎ", "delivery link"),
                    SearchWord("버튼 클릭 생활용품 공동구매", "delivery link")
            )
            binding.recycelerView.adapter = SearchAdapter(searchWordList)
        }

        btnButton2.setOnClickListener{
            btnButton2.setBackgroundResource(R.drawable.button_round1_gray)
            btnButton2.setTextColor(Color.parseColor("#FFFFFF"))
            btnButton1.setBackgroundResource((R.drawable.button_round1_white))
            btnButton1.setTextColor(Color.parseColor("#000000"))
            //버튼에 따른 키워드를 넣는 공간(이동될 layout(?)도)
            val searchWordList = arrayListOf(
                    SearchWord("버튼2 클릭 당근", "carrot link"),
                    SearchWord("버튼2 클릭 배민", "delivery link"),
                    SearchWord("버튼2 클릭 요기요", "delivery link"),
                    SearchWord("버튼2 클릭 ㅎㅎ", "delivery link"),
                    SearchWord("버튼2 클릭 생활용품 공동구매", "delivery link")
            )
            binding.recycelerView.adapter = SearchAdapter(searchWordList)
        }

        val searchWordList = arrayListOf(
                SearchWord("당근마켓", "carrot link"),
                SearchWord("배민", "delivery link"),
                SearchWord("요기요", "delivery link"),
                SearchWord("ㅎㅎ", "delivery link"),
                SearchWord("생활용품 공동구매", "delivery link")
        )

        val flexBoxLayoutManager = FlexboxLayoutManager(mainContext) as FlexboxLayoutManager
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
        flexBoxLayoutManager.alignItems = AlignItems.CENTER
        flexBoxLayoutManager.justifyContent = JustifyContent.CENTER
        binding.recycelerView.layoutManager = flexBoxLayoutManager
        //binding.recycelerView.setHasFixedSize(true)
        binding.recycelerView.adapter = SearchAdapter(searchWordList)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context as FrameMain
    }

}