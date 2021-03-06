package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
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
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cookandroid.itshow_tob.databinding.WriteRecruitmentBinding
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        val img_SearchBack = findViewById<ImageView>(R.id.img_SearchBack)

        img_SearchBack.setOnClickListener(View.OnClickListener{
            onBackPressed()
        })

        //?????????
        val infoView = layoutInflater.inflate(R.layout.dialog_information, null)
        val textContent_info = infoView.findViewById<TextView>(R.id.textContent)
        val btnOk_info = infoView.findViewById<TextView>(R.id.btnOk)
        val infoDialog = AlertDialog.Builder(this).setView(infoView).create()
        btnOk_info.setOnClickListener{infoDialog.dismiss()}

        Log.d("ToB", "????????? ??????")

        edit_search.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    layout_recycler.visibility = View.INVISIBLE
                    layout_button.visibility = View.VISIBLE

                    Log.d("TAG", "?????????")
                }
            }
        })

        //?????? ?????? ??????
        btn_search.setOnClickListener{
            if(edit_search.text.isBlank()) {
                textContent_info.setText("???????????? ??????????????????!")
                infoDialog.show()
            }else {
                edit_search.clearFocus()
                layout_recycler.visibility = View.VISIBLE
                layout_button.visibility = View.INVISIBLE

                //????????? ?????? ??????
                val loadingDialog = LoadingDialog(this)
                //????????????
                loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val apiService = retrofit.create(SearchAPIService::class.java)
                loadingDialog.show()

                val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL )
                recyclerSearchRecruitment.layoutManager = staggeredGridLayoutManager

                // ????????? ??????
                val apiCallSearchData = apiService.recruitmentSearch(edit_search.text.toString())
                Log.d("ToB sdsd", edit_search.text.toString())

                apiCallSearchData.enqueue(object : Callback<JsonArray> {
                    override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                        Toast.makeText(this@Search, "?????? ?????? ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ToB", t.message.toString())
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                        val data = response.body() // ??????????????? ?????? ????????? ????????? ????????????. RecruitmentAPIJSONResponseFromGSON????????? ?????????.
                        if(data != null){
                            //Gson??? ????????? JSON ???????????? ???????????? ??? ????????? ??? ?????? ???????????????????????????.
                            //api??? ???????????? ???????????? ????????? ??????????????? ???????????? ??? ?????? ?????? ???????????? RecruitmentDatas?????? ????????? ????????? ?????? ???????????????.
                            val gson = Gson()

                            Log.d("ToB sdsd", "?????????")

                            var mainList = arrayListOf<MainData>();

                            for (i in 0 until data.size()){

                                val recruitmentData = gson.fromJson(data.get(i).toString(), RecruitmentDatas::class.java)

                                val r_no = recruitmentData.r_no
                                val title = recruitmentData.r_title.toString()
                                val email = recruitmentData.u_email.toString()
                                val content = recruitmentData.r_content?.toString()
                                val imgPath = recruitmentData.r_imgPath?.toString()
                                var minPrice = ""
                                if(recruitmentData.r_minPrice.toString().length == 0){ minPrice = "" }
                                val format = DecimalFormat("###,###")
                                minPrice = format.format(recruitmentData.r_minPrice.toString().toLong()) + "???"

                                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                                val endDate = dateFormat.parse(recruitmentData.r_endDate).time
                                val today = Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }.time.time

                                val date = "~"+((endDate - today) / (24 * 60 * 60 * 1000)).toString()+"???"
                                val location = recruitmentData.r_location
                                mainList.add(MainData(r_no, email, title, minPrice, content, location, date, imgPath))
                            }
                            recyclerSearchRecruitment.adapter = MainCustomAdapter(this@Search, mainList)
                        }
                    }
                })

                // ????????? ??????
                val apiCallForData = apiService.newSearchWord(CreateSearchWord(USER_EMAIL, (edit_search.text.toString()).trim()))
                // ????????? ??????
                apiCallForData.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(this@Search, "?????? ?????? ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ToB", t.message.toString())
                        loadingDialog.dismiss()
                    }

                    @RequiresApi(O)
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("ToB", response.message())
                        val color = btn_recentlySearch.currentTextColor
                        val hexColor = String.format("#%06X", (color))
                        Log.d("ToB", hexColor)
                        //????????? ?????? ?????????
                        if (hexColor.equals("#FFFFFFFF")) {
                            //?????? ????????? ????????? ???????????? ??????
                            btn_recentlySearch.callOnClick()
                        } else {
                            //?????? ????????? ????????? ???????????? ??????
                            btn_popularitySearch.callOnClick()
                        }
                        edit_search.setText("")
                        loadingDialog.dismiss()
                    }
                })

            }
        }

        //?????? ?????????
        btn_recentlySearch.setOnClickListener{
            //????????? ?????? ??????
            val loadingDialog = LoadingDialog(this)
            //????????????
            loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            //?????? ??? ?????????
            btn_recentlySearch.setBackgroundResource(R.drawable.button_round1_blue)
            btn_recentlySearch.setTextColor(Color.parseColor("#FFFFFF"))
            btn_popularitySearch.setBackgroundResource((R.drawable.button_round1_white))
            btn_popularitySearch.setTextColor(Color.parseColor("#000000"))

            val apiService = retrofit.create(SearchAPIService::class.java)
            loadingDialog.show()
            val apiCallForData = apiService.searchRecent(USER_EMAIL)

            apiCallForData.enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Toast.makeText(this@Search, "?????? ?????? ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ToB", t.message.toString())
                    loadingDialog.dismiss()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val data = response.body() // ??????????????? ?????? ????????? ????????? ????????????. RecruitmentAPIJSONResponseFromGSON????????? ?????????.
                    if(data != null){
                        //Gson??? ????????? JSON ???????????? ???????????? ??? ????????? ??? ?????? ???????????????????????????.
                        //api??? ???????????? ???????????? ????????? ??????????????? ???????????? ??? ?????? ?????? ???????????? RecruitmentDatas?????? ????????? ????????? ?????? ???????????????.
                        val gson = Gson()

                        val searchWordList:ArrayList<SearchWord> = arrayListOf()
                        Log.d("ToB", data.toString())
                        var len = data.size()
                        if(len > 8) len = 8
                        for(idx in 0 until len){//?????? 5???
                            searchWordList.add(SearchWord(gson.fromJson(data.get(idx).toString(), SearchWordData::class.java).s_word))
                        }
                        binding.recycelerView.adapter = SearchAdapter(searchWordList)
                    }
                    loadingDialog.dismiss()
                }
            })
        }

        //?????? ?????????
        btn_popularitySearch.setOnClickListener{
            //????????? ?????? ??????
            val loadingDialog = LoadingDialog(this)
            //????????????
            loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            btn_popularitySearch.setBackgroundResource(R.drawable.button_round1_blue)
            btn_popularitySearch.setTextColor(Color.parseColor("#FFFFFF"))
            btn_recentlySearch.setBackgroundResource((R.drawable.button_round1_white))
            btn_recentlySearch.setTextColor(Color.parseColor("#000000"))
            //????????? ?????? ?????????

            val apiService = retrofit.create(SearchAPIService::class.java)
            loadingDialog.show()
            val apiCallForData = apiService.searchPopular()

            apiCallForData.enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Toast.makeText(this@Search, "?????? ?????? ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ToB", t.message.toString())
                    loadingDialog.dismiss()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    val data = response.body() // ??????????????? ?????? ????????? ????????? ????????????. RecruitmentAPIJSONResponseFromGSON????????? ?????????.
                    if(data.toString() != "[]"){
                        //Gson??? ????????? JSON ???????????? ???????????? ??? ????????? ??? ?????? ???????????????????????????.
                        //api??? ???????????? ???????????? ????????? ??????????????? ???????????? ??? ?????? ?????? ???????????? RecruitmentDatas?????? ????????? ????????? ?????? ???????????????.
                        val gson = Gson()
                        val searchWordList:ArrayList<SearchWord> = arrayListOf()
                        Log.d("ToB", data.toString())
                        var len = data!!.size()
                        if(len > 8) len = 8
                        for(idx in 0 until len){//?????? 5???
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Search, FrameMain::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}
