package com.cookandroid.itshow_tob

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailsRecruitment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_recruitment)

        var togle = ""
        var count = ""
        val textTitle = findViewById<TextView>(R.id.text_Title_details)
        val textContent = findViewById<TextView>(R.id.text_value_details)
        val textMinAmount = findViewById<TextView>(R.id.text_minAmount_details)
        val textUserName = findViewById<TextView>(R.id.text_userName)
        val textOrder = findViewById<TextView>(R.id.text_order_details)
        val textDate = findViewById<TextView>(R.id.text_date_details)
        val textLocation = findViewById<TextView>(R.id.text_loc_details)
        val textCategory = findViewById<TextView>(R.id.text_category_details)
        val text_img = findViewById<ImageView>(R.id.img_recruitment)
        val img_heart = findViewById<ImageView>(R.id.img_heart)
        val text_heartNum = findViewById<TextView>(R.id.text_heartNum)
        val btn_chat = findViewById<TextView>(R.id.btn_chat)

        //no을 통해 게시글 정보를 불러오는 코드
        val r_no = intent.getSerializableExtra("r_no") as Int
        Log.d(TAG, "r_no : "+r_no.toString());


        textUserName.setOnClickListener(View.OnClickListener {
            var intent = Intent(this, UserInfoActicity::class.java)
            intent.putExtra("u_name", textUserName.text)
            intent.putExtra("preEmail", "")
            intent.putExtra("preFollow", "")
            startActivity(intent)
        })

        btn_chat.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("r_no", r_no.toString())
            startActivity(intent)
        })

        val apiService = retrofit.create(RecruitmentAPIService::class.java)
        val apiService2 = retrofit.create(Post::class.java)

        //넘버가 r_no인 게시물을 가지고옴
        val apiCallForData = apiService.getRecruitmentInfo(r_no)

        apiCallForData.enqueue(object :Callback<JsonArray>{
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Toast.makeText(this@DetailsRecruitment, "에러 발생 ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("dahuin", t.message.toString())
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.

                if(data != null){

                    //Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
                    //api가 처음부터 리스트의 형태로 되어있어서 리스트의 첫 번째 값을 가져온후 RecruitmentDatas에서 선언한 변수와 자동 매핑해준다.
                    val gson = Gson()
                    val recruitmentData = gson.fromJson(data.get(0).toString(), RecruitmentDatas::class.java)

                    val title = recruitmentData.r_title.toString()
                    val content = recruitmentData.r_content?.toString()

                    var minPrice = recruitmentData.r_minPrice.toString()+"원"
                    if(recruitmentData.r_minPrice.toString().length == 0){ minPrice = "" }
                    val format = DecimalFormat("###,###")
                    minPrice = format.format(recruitmentData.r_minPrice.toString().toLong()) + "원"

                    val email = recruitmentData.u_email.toString()
                    val endDate = recruitmentData.r_endDate.toString()
                    val order = recruitmentData.r_order.toString()
                    val location = recruitmentData.r_location.toString()
                    val category = recruitmentData.r_category?.toString()
                    val imgPath = recruitmentData.r_imgPath?.toString()
                    val name = recruitmentData.u_name?.toString()

                    Log.d("dahuin", title)
                    Log.d("dahuin", content?:"")
                    Log.d("dahuin", minPrice)

                    //endDate를 format
                    val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a 까지")
                    val date = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME).format(sdf)

                    if(imgPath != ""){
                        text_img.visibility = View.GONE
                    }
                    textTitle.text = title
                    textContent.text = content?:""
                    textMinAmount.text = minPrice
                    textUserName.text = name
                    textDate.text = date.toString()
                    textOrder.text = order
                    textLocation.text = location
                    textCategory.text = "카테고리 > "+category?:"없음"

                    val apiCallForData2 = apiService2.post_like(r_no)
                    apiCallForData2.enqueue(object :Callback<JsonArray>{
                        override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                            val data = response.body()

                            count = data.toString().substring(5,data.toString().length-2)
                            if(count != "0") {
                                togle = "1"
                            } else {
                                togle = "0"
                            }
                            Log.d("data count", count)

                            when(togle){
                                "0" -> {
                                    text_heartNum.text = count
                                }
                                "1" -> {
                                    text_heartNum.text = count
                                }
                            }
                        }
                        override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                        }
                    })

                    val apiCallForData3 = apiService2.post_like_user(r_no, USER_EMAIL)
                    apiCallForData3.enqueue(object :Callback<JsonArray>{
                        override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                            val data = response.body()

                            var count = data.toString().substring(5,data.toString().length-2)
                            if(count != "0") {
                                togle = "1"
                            } else {
                                togle = "0"
                            }
                            Log.d("data 실행", data.toString())

                            when(togle){
                                "0" -> {
                                    img_heart.setImageResource(R.drawable.bin_heart)
                                    text_heartNum.setTextColor(Color.parseColor("#000000"))
                                }
                                "1" -> {
                                    img_heart.setImageResource(R.drawable.heart)
                                    text_heartNum.setTextColor(Color.parseColor("#ffffff"))

                                }
                            }
                        }
                        override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                        }
                    })
                }
            }
        })
        //no을 통해 게시글 정보를 불러오는 코드 END

        val img_DetailsBack = findViewById<ImageView>(R.id.img_DetailsBack)
        img_DetailsBack.setOnClickListener {
            finish()
        }

        img_heart.setOnClickListener(View.OnClickListener{
            when(togle){
                "0" -> { // 찜 눌렀을 때
                    togle = "1"
                    img_heart.setImageResource(R.drawable.heart)
                    text_heartNum.setTextColor(Color.parseColor("#ffffff"))
                    text_heartNum.text = (count.toInt()+1).toString()

                    val apiCallForData2 = apiService2.postLikeState(postLikeData(r_no, USER_EMAIL))
                    apiCallForData2.enqueue(object :Callback<postLikeData> {
                        override fun onResponse(call: Call<postLikeData>, response: Response<postLikeData>) {
                            Log.d(TAG, "성공 ${response.raw()}")
                        }
                        override fun onFailure(call: Call<postLikeData>, t: Throwable) {
                            Log.d(TAG, "실패")
                        }
                    })
                    Log.d(TAG, "hi"+count)
                    count = (count.toInt()+1).toString()
                }
                "1" -> {
                    togle = "0"
                    img_heart.setImageResource(R.drawable.bin_heart)
                    text_heartNum.setTextColor(Color.parseColor("#000000"))
                    text_heartNum.text = (count.toInt()-1).toString()

                    val apiCallForData2 = apiService2.postLikeState(postLikeData(r_no, USER_EMAIL))
                    apiCallForData2.enqueue(object :Callback<postLikeData> {
                        override fun onResponse(call: Call<postLikeData>, response: Response<postLikeData>) {
                            Log.d(TAG, "성공 ${response.raw()}")
                        }
                        override fun onFailure(call: Call<postLikeData>, t: Throwable) {
                            Log.d(TAG, "실패")
                        }
                    })
                    Log.d(TAG, "hi"+count)
                    count = (count.toInt()-1).toString()

                }
            }
        })
    }
}
