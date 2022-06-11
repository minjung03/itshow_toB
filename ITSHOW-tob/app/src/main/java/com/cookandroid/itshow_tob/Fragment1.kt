package com.cookandroid.itshow_tob

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.main_recycler.*
import kotlinx.android.synthetic.main.main_recycler.view.*


class Fragment1 : Fragment() {


    private var mainList = arrayListOf<MainData>(
            MainData("[급구] 크리넥스 휴지 120롤 나눠사실 분!!",
                    "12000", "크리넥스 30롤 4개 4만원에 판매하는거 나눠사실 분 채팅주세요 :) 사이트는 아래 첨부",
                    "관악구", 12, "18일"),

            MainData("생수 공구 구해요",
                    "가격 없음", "생수 공구 모집하시는 분 연락주세요.. 칼답가능!",
                    "미림 3동", 0, "기한 없음"),

            MainData("쿠팡 생필품 공구 모집",
                    "50000", "사진 속 쿠팡 마켓에서 생필품 같이 사실 분 구해요! 미림 아파트 주민, 근처 사시는 분들 ...",
                    "미림 3동", 10, "23일"),

            MainData("[급구] 크리넥스 휴지 120롤 나눠사실 분!!",
                    "12000", "크리넥스 30롤 4개 4만원에 판매하는거 나눠사실 분 채팅주세요 :) 사이트는 아래 첨부",
                    "관악구", 12, "18일"),

            MainData("생수 공구 구해요",
                    "가격 없음", "생수 공구 모집하시는 분 연락주세요... 칼답가능!",
                    "미림 3동", 0, "기한 없음"),

            MainData("쿠팡 생필품 공구 모집",
                    "50000", "사진 속 쿠팡 마켓에서 생필품 같이 사실 분 구해요! 미림 아파트 주민, 근처 사시는 분들 ...",
                    "미림 3동", 10, "23일")
    )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {

        val view = inflater.inflate(R.layout.main_recycler, container, false)

        val mAdapter = MainCustomAdapter(activity as Context, mainList)
        view.recyceler_view_main.adapter = mAdapter

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
        view.recyceler_view_main.layoutManager = staggeredGridLayoutManager

        return view
    }

}