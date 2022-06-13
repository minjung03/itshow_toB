package com.cookandroid.itshow_tob

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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

class Fragment1 : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000") //로컬호스트로 접속하기 위해!
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RecruitmentAPIService::class.java)
        val apiCallForData = apiService.getRecruitmentList()


        //recyclerview
        val view = inflater.inflate(R.layout.main_recycler, container, false)

        val recyclerView_main = view.findViewById<RecyclerView>(R.id.recyceler_view_main)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
        recyclerView_main.layoutManager = staggeredGridLayoutManager

        apiCallForData.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                // Toast.makeText(this@Fragment1, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("에러 발생", t.message.toString())
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.

                if(data != null){

                    //Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
                    //api가 처음부터 리스트의 형태로 되어있어서 리스트의 첫 번째 값을 가져온후 RecruitmentDatas에서 선언한 변수와 자동 매핑해준다.
                    // Log.d("데이터 : ", data.toString())

                    val gson = Gson()
                    var mainList = arrayListOf<MainData>();

                    for (i in 0 until data.size()){

                        val recruitmentData = gson.fromJson(data.get(i).toString(), RecruitmentDatas::class.java)

                        val title = recruitmentData.r_title.toString()
                        val content = recruitmentData.r_content?.toString()
                        var minPrice = recruitmentData.r_minPrice.toString()+"원"
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

                        val date = ((endDate - today) / (24 * 60 * 60 * 1000) +1).toString()
                        Log.d("날짜 : ", date)

                        val location = recruitmentData.r_location.toString()

                        Log.d("데이터 : ", title+" "+minPrice+" "+content+" "+location+" "+date)

                        mainList.add(MainData(title, minPrice, content, location, 0, "date"))

                    }


                    val mAdapter = MainCustomAdapter(activity as Context, mainList)
                    recyclerView_main.adapter = mAdapter


                }
            }
        })
        return view
    }

}