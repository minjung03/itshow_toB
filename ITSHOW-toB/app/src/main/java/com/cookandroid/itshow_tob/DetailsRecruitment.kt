package com.cookandroid.itshow_tob

import android.os.Build
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailsRecruitment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_recruitment)

        val textTitle = findViewById<TextView>(R.id.text_Title_details)
        val textContent = findViewById<TextView>(R.id.text_value_details)
        val textMinAmount = findViewById<TextView>(R.id.text_minAmount_details)
        val textUserName = findViewById<TextView>(R.id.text_userName)
        val textOrder = findViewById<TextView>(R.id.text_order_details)
        val textDate = findViewById<TextView>(R.id.text_date_details)
        val textLocation = findViewById<TextView>(R.id.text_loc_details)
        val textCategory = findViewById<TextView>(R.id.text_category_details)

        //no을 통해 게시글 정보를 불러오는 코드
        val r_no = 3 //temp

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000") //로컬호스트로 접속하기 위해!
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiService = retrofit.create(RecruitmentAPIService::class.java)

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

                    Log.d("dahuin", title)
                    Log.d("dahuin", content?:"")
                    Log.d("dahuin", minPrice)

                    //endDate를 format
                    val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a 까지")
                    val date = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME).format(sdf)

                    textTitle.text = title
                    textContent.text = content?:""
                    textMinAmount.text = minPrice
                    textUserName.text = email//임시
                    textDate.text = date.toString()
                    textOrder.text = order
                    textLocation.text = location
                    textCategory.text = "카테고리 > "+category?:"없음"

                }
            }


        })

        //no을 통해 게시글 정보를 불러오는 코드 END

    }


}
