package com.cookandroid.itshow_tob

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Fragment1() : Fragment() {

    lateinit var recyclerView_main:RecyclerView
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        Log.d("TOB", "카테고리 전체 들어옴")

        var count = ""
        var togle = ""
        var mAdapter : MainCustomAdapter
        //recyclerview
        val view = inflater.inflate(R.layout.main_recycler, container, false)
        recyclerView_main = view.findViewById<RecyclerView>(R.id.recyceler_view_main)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
        recyclerView_main.layoutManager = staggeredGridLayoutManager

        val apiService = retrofit.create(RecruitmentAPIService::class.java)
        var apiCallForData = apiService.getRecruitmentList()

        apiCallForData.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.d("에러 발생", t.message.toString())
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.

                if(data != null){

                    val gson = Gson()
                    var mainList = arrayListOf<MainData>();

                    for (i in 0 until data.size()){

                        val recruitmentData = gson.fromJson(data.get(i).toString(), RecruitmentDatas::class.java)

                        val r_no = recruitmentData.r_no
                        val title = recruitmentData.r_title.toString()
                        val email = recruitmentData.u_email.toString()
                        var content = recruitmentData.r_content?.toString()
                        var imgPath = recruitmentData.r_imgPath?.toString()
                        var minPrice = ""
                        if(recruitmentData.r_minPrice.toString().length == 0){ minPrice = "" }
                        val format = DecimalFormat("###,###")
                        minPrice = format.format(recruitmentData.r_minPrice.toString().toLong()) + "원"

                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                        val endDate = dateFormat.parse(recruitmentData.r_endDate).time
                        val today = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }.time.time

                        val date = "~"+((endDate - today) / (24 * 60 * 60 * 1000)+1).toString()+"일"
                        val location = recruitmentData.r_location

                        if(content.length >= 30){
                            content = content.substring(0,30)+"...";
                        }
                        mainList.add(MainData(r_no, email, title, minPrice, content, location, date, imgPath))

                    }
                    mAdapter = MainCustomAdapter(activity as Context, mainList)
                    recyclerView_main.adapter = mAdapter
                }
            }
        })
        return view
    }
}