package com.cookandroid.itshow_tob

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.itshow_tob.databinding.SearchBinding
import androidx.core.app.ComponentActivity.ExtraData
import android.os.Build.VERSION_CODES.O
import com.cookandroid.itshow_tob.Search
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.cookandroid.itshow_tob.databinding.WriteRecruitmentBinding
import com.google.android.flexbox.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Search : AppCompatActivity() {
    private var sBinding: SearchBinding? = null
    private val binding get() = sBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sBinding = SearchBinding.inflate(layoutInflater)
        setContentView( binding.root)

        val edit_search = findViewById<EditText>(R.id.edit_search)
        val btn_search= findViewById<Button>(R.id.btn_search)
        val btn_recentlySearch = findViewById<Button>(R.id.btn_recentlySearch)
        val btn_popularitySearch = findViewById<Button>(R.id.btn_popularitySearch)

        val u_email = "dahuin" //temp

        //검색 버튼 클릭
        btn_search.setOnClickListener{
            //로딩창 객체 생성
            val loadingDialog = LoadingDialog(this)
            //투명하게
            loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            //검색어 저장
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000") //로컬호스트로 접속하기 위해!
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val apiService = retrofit.create(SearchAPIService::class.java)
            loadingDialog.show()
            val apiCallForData = apiService.newSearchWord(CreateSearchWord(u_email, edit_search.text.toString()))

            apiCallForData.enqueue(object : Callback<CreateSearchWord> {
                override fun onFailure(call: Call<CreateSearchWord>, t: Throwable) {
                    Toast.makeText(this@Search, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ToB", t.message.toString())
                    loadingDialog.dismiss()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<CreateSearchWord>, response: Response<CreateSearchWord>) {
                    Toast.makeText(this@Search, "성공", Toast.LENGTH_SHORT).show()
                    Log.d("ToB", response.message())
                    loadingDialog.dismiss()
                }
            })
        }

        //최근 검색어
        btn_recentlySearch.setOnClickListener{
            //로딩창 객체 생성
            val loadingDialog = LoadingDialog(this)
            //투명하게
            loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            //버튼 색 바꾸기
            btn_recentlySearch.setBackgroundResource(R.drawable.button_round1_gray)
            btn_recentlySearch.setTextColor(Color.parseColor("#FFFFFF"))
            btn_popularitySearch.setBackgroundResource((R.drawable.button_round1_white))
            btn_popularitySearch.setTextColor(Color.parseColor("#000000"))

            //특정 사용자의 최근 검색어를 가져옴
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000") //로컬호스트로 접속하기 위해!
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val apiService = retrofit.create(SearchAPIService::class.java)
            loadingDialog.show()
            val apiCallForData = apiService.searchRecent(u_email)

            apiCallForData.enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Toast.makeText(this@Search, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ToB", t.message.toString())
                    loadingDialog.dismiss()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.
                    if(data != null){
                        //Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
                        //api가 처음부터 리스트의 형태로 되어있어서 리스트의 첫 번째 값을 가져온후 RecruitmentDatas에서 선언한 변수와 자동 매핑해준다.
                        val gson = Gson()

                        val searchRecentData = gson.fromJson(data.get(0).toString(), SearchWordData::class.java)
                        val searchWordList:ArrayList<SearchWord> = arrayListOf()
                        Log.d("ToB", data.toString())
                        for(idx in 0 until 5){//5개
                            searchWordList.add(SearchWord(gson.fromJson(data.get(idx).toString(), SearchWordData::class.java).s_word))
                        }

                        binding.recycelerView.adapter = SearchAdapter(searchWordList)
                    }
                    loadingDialog.dismiss()
                }
            })
        }

        //인기 검색어
        btn_popularitySearch.setOnClickListener{
            //로딩창 객체 생성
            val loadingDialog = LoadingDialog(this)
            //투명하게
            loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            btn_popularitySearch.setBackgroundResource(R.drawable.button_round1_gray)
            btn_popularitySearch.setTextColor(Color.parseColor("#FFFFFF"))
            btn_recentlySearch.setBackgroundResource((R.drawable.button_round1_white))
            btn_recentlySearch.setTextColor(Color.parseColor("#000000"))
            //버튼에 따른 키워드

            //인기검색어
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000") //로컬호스트로 접속하기 위해!
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val apiService = retrofit.create(SearchAPIService::class.java)
            loadingDialog.show()
            val apiCallForData = apiService.searchPopular()

            apiCallForData.enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Toast.makeText(this@Search, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ToB", t.message.toString())
                    loadingDialog.dismiss()

                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.
                    if(data != null){
                        //Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
                        //api가 처음부터 리스트의 형태로 되어있어서 리스트의 첫 번째 값을 가져온후 RecruitmentDatas에서 선언한 변수와 자동 매핑해준다.
                        val gson = Gson()

                        val searchRecentData = gson.fromJson(data.get(0).toString(), SearchWordData::class.java)
                        val searchWordList:ArrayList<SearchWord> = arrayListOf()
                        Log.d("ToB", data.toString())
                        for(idx in 0 until 5){//5개
                            searchWordList.add(SearchWord(gson.fromJson(data.get(idx).toString(), SearchWordData::class.java).s_word))
                        }

                        binding.recycelerView.adapter = SearchAdapter(searchWordList)
                    }
                    loadingDialog.dismiss()
                }
            })
        }

        val flexBoxLayoutManager = FlexboxLayoutManager(this@Search) as FlexboxLayoutManager
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP
        flexBoxLayoutManager.alignItems = AlignItems.CENTER
        flexBoxLayoutManager.justifyContent = JustifyContent.CENTER
        binding.recycelerView.layoutManager = flexBoxLayoutManager
        //binding.recycelerView.setHasFixedSize(true)
        btn_recentlySearch.callOnClick()
    }

}