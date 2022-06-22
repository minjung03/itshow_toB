package com.cookandroid.itshow_tob

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HeartList : Fragment(){

    lateinit var recyceler_heart_list: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {

        var togle = 0 // 0 : 인기, 1: 최근

        val view = inflater.inflate(R.layout.heart_list, container, false)
        // HeartListSetting(view, 0)
        recyceler_heart_list = view.findViewById<RecyclerView>(R.id.recyceler_heart_list)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL )
        recyceler_heart_list.layoutManager = staggeredGridLayoutManager

        val heart_list_popular = view.findViewById<Button>(R.id.heart_list_popular)
        val heart_list_recent = view.findViewById<Button>(R.id.heart_list_recent)

        HeartListSetting(view, togle)

        heart_list_popular.setOnClickListener(View.OnClickListener {
            togle = 0
            HeartListSetting(view, togle)
        })
        heart_list_recent.setOnClickListener(View.OnClickListener {
            togle = 1
            HeartListSetting(view, togle)
        })

        return view
    }

    fun HeartListSetting(view : View, togle : Int) {
        var auth: FirebaseAuth? = FirebaseAuth.getInstance()

        var apiCallForData : Call<JsonArray>
        val apiService = retrofit.create(Post::class.java)
        var Adapter : HeartListAdapter

        var user = auth!!.currentUser
        user?.let {
            var email = user.email.toString()

            Log.d("togle ", togle.toString())
            apiCallForData = apiService.postList(email, togle)

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

                            if(content.length >= 40){
                                content = content.substring(0,40)+"...";
                            }

                            mainList.add(MainData(r_no, email, title, minPrice, content, location, date, imgPath))
                        }
                        Adapter = HeartListAdapter(activity as Context, mainList)
                        // recyceler_heart_list.adapter = Adapter
                        recyceler_heart_list.swapAdapter(Adapter, false)
                    }
                }
            })
        }
    }

}