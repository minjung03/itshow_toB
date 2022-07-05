package com.cookandroid.itshow_tob

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val TAG = "ToB";
var auth: FirebaseAuth? = FirebaseAuth.getInstance()
var USER_EMAIL = auth?.currentUser?.email.toString()
var USER_NAME = auth?.currentUser?.displayName.toString()
var USER_IMG = ""

//Fragment2~6에서 사용하는 코드
fun getRecruitmentItemList(recyclerView_main: RecyclerView, category:String, activity: Context) {
    var mAdapter: MainCustomAdapter

    //recyclerview
    val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    recyclerView_main.layoutManager = staggeredGridLayoutManager

    val apiService = retrofit.create(RecruitmentAPIService::class.java)
    var apiCallForData = apiService.getCategoryRecruitment(category)

    apiCallForData.enqueue(object : Callback<JsonArray> {
        override fun onFailure(call: Call<JsonArray>, t: Throwable) {
            Log.d("에러 발생", t.message.toString())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
            val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.

            if (data != null) {

                val gson = Gson()
                var mainList = arrayListOf<MainData>();

                for (i in 0 until data.size()) {

                    val recruitmentData = gson.fromJson(data.get(i).toString(), RecruitmentDatas::class.java)

                    val r_no = recruitmentData.r_no
                    val title = recruitmentData.r_title.toString()
                    var content = recruitmentData.r_content?.toString()
                    val imgPath = recruitmentData.r_imgPath?.toString()
                    val email = recruitmentData.u_email.toString()

                    var minPrice = ""
                    if (recruitmentData.r_minPrice.toString().length == 0) {
                        minPrice = ""
                    }
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

                    val date = "~" + ((endDate - today) / (24 * 60 * 60 * 1000)).toString() + "일"
                    val location = recruitmentData.r_location

                    if(content.length >= 30){
                        content = content.substring(0,30)+"...";
                    }

                    mainList.add(MainData(r_no, email, title, minPrice, content, location, date, imgPath))

                }
                mAdapter = MainCustomAdapter(activity, mainList)
                recyclerView_main.adapter = mAdapter
            }
        }
    })
}