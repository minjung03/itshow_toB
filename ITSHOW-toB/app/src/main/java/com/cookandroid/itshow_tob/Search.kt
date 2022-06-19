package com.cookandroid.itshow_tob

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.itshow_tob.databinding.SearchBinding
import android.os.Build.VERSION_CODES.O
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.flexbox.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Search : AppCompatActivity() {

    private var sBinding: SearchBinding? = null
    private val binding get() = sBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sBinding = SearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layout_button = findViewById<LinearLayout>(R.id.layout_search_button);
        val layout_recycler = findViewById<LinearLayout>(R.id.layout_search_recycler);
        val recyclerSearchRecruitment = findViewById<RecyclerView>(R.id.recyceler_view_search)
        val edit_search = findViewById<EditText>(R.id.edit_search)
        val btn_search= findViewById<Button>(R.id.btn_search)
        val btn_recentlySearch = findViewById<Button>(R.id.btn_recentlySearch)
        val btn_popularitySearch = findViewById<Button>(R.id.btn_popularitySearch)

        val u_email = "s2003@e-mirim.hs.kr" //temp

        //정보창
        val infoView = layoutInflater.inflate(R.layout.dialog_information, null)
        val textContent_info = infoView.findViewById<TextView>(R.id.textContent)
        val btnOk_info = infoView.findViewById<TextView>(R.id.btnOk)
        val infoDialog = AlertDialog.Builder(this).setView(infoView).create()
        btnOk_info.setOnClickListener{infoDialog.dismiss()}

        Log.d("ToB", "포커스 실행")

        edit_search.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    layout_recycler.visibility = View.INVISIBLE
                    layout_button.visibility = View.VISIBLE

                    Log.d("TAG", "포커스")
                }
            }
        })

        //검색 버튼 클릭
        btn_search.setOnClickListener{
            if(edit_search.text.isBlank()) {
                textContent_info.setText("검색어를 입력해주세요!")
                infoDialog.show()
            }else {
                edit_search.clearFocus()
                layout_recycler.visibility = View.VISIBLE
                layout_button.visibility = View.INVISIBLE

                //로딩창 객체 생성
                val loadingDialog = LoadingDialog(this)
                //투명하게
                loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val apiService = retrofit.create(SearchAPIService::class.java)
                loadingDialog.show()

                val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
                recyclerSearchRecruitment.layoutManager = staggeredGridLayoutManager


                // 게시글 검색
                val apiCallSearchData = apiService.recruitmentSearch(edit_search.text.toString())
                apiCallSearchData.enqueue(object : Callback<JsonArray> {
                    override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                        Toast.makeText(this@Search, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ToB", t.message.toString())
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                        val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.
                        if(data != null){
                            //Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
                            //api가 처음부터 리스트의 형태로 되어있어서 리스트의 첫 번째 값을 가져온후 RecruitmentDatas에서 선언한 변수와 자동 매핑해준다.
                            val gson = Gson()

                            var mainList = arrayListOf<MainData>();

                            for (i in 0 until data.size()){

                                val recruitmentData = gson.fromJson(data.get(i).toString(), RecruitmentDatas::class.java)

                                val r_no = recruitmentData.r_no
                                val title = recruitmentData.r_title.toString()
                                val content = recruitmentData.r_content?.toString()
                                val imgPath = recruitmentData.r_imgPath?.toString()
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

                                val date = "~"+((endDate - today) / (24 * 60 * 60 * 1000)).toString()+"일"
                                val location = recruitmentData.r_location
                                mainList.add(MainData(r_no, title, minPrice, content, location, 0, date, imgPath))
                            }
                            recyclerSearchRecruitment.adapter = MainCustomAdapter(this@Search, mainList)
                            (recyclerSearchRecruitment.adapter as MainCustomAdapter).notifyItemChanged(2)
                        }
                    }
                })

                // 검색어 저장
                val apiCallForData = apiService.newSearchWord(CreateSearchWord(u_email, (edit_search.text.toString()).trim()))
                // 검색어 저장
                apiCallForData.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@Search, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ToB", t.message.toString())
                        loadingDialog.dismiss()
                    }

                    @RequiresApi(O)
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("ToB", response.message())
                        val color = btn_recentlySearch.currentTextColor
                        val hexColor = String.format("#%06X", (color))
                        Log.d("ToB", hexColor)
                        //검색어 정보 재요청
                        if (hexColor.equals("#FFFFFFFF")) {
                            //최근 검색어 버튼이 눌려있는 상태
                            btn_recentlySearch.callOnClick()
                        } else {
                            //인기 검색어 버튼이 눌려있는 상태
                            btn_popularitySearch.callOnClick()
                        }
                        edit_search.setText("")
                        loadingDialog.dismiss()
                    }
                })

            }
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
                    .baseUrl("http://10.0.2.2:3003") //로컬호스트로 접속하기 위해!
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

                        val searchWordList:ArrayList<SearchWord> = arrayListOf()
                        Log.d("ToB", data.toString())
                        var len = data.size()
                        if(len > 5) len = 5
                        for(idx in 0 until len){//최대 5개
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
                        var len = data.size()
                        if(len > 5) len = 5
                        for(idx in 0 until len){//최대 5개
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
