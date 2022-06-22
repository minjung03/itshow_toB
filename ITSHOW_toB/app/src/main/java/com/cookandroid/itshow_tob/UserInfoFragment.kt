package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.willy.ratingbar.BaseRatingBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class UserInfoFragment : Fragment() {

    var auth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_info_frgment, container, false)

        val settings_icon = view.findViewById<ImageView>(R.id.image_settings)
        settings_icon.setOnClickListener {
            val intent = Intent(activity, Setting::class.java)
            startActivity(intent)
        }

        var user_name: TextView = view.findViewById(R.id.user_info_name)
        var user_image: ImageView = view.findViewById(R.id.user_info_Image)
        var ratingbar_user: BaseRatingBar = view.findViewById(R.id.ratingbar_user)
        val layout_follower_info = view.findViewById<LinearLayout>(R.id.layout_follower_info)
        val layout_following_info = view.findViewById<LinearLayout>(R.id.layout_following_info)

        ratingbar_user.rating = 3.5f

        layout_follower_info.setOnClickListener{
            val intent = Intent(activity, FollowList::class.java)
            intent.putExtra("follow", "follower")
            intent.putExtra("u_email", USER_EMAIL)
            startActivity(intent)
        }

        layout_following_info.setOnClickListener{
            val intent = Intent(activity, FollowList::class.java)
            intent.putExtra("follow", "following")
            intent.putExtra("u_email", USER_EMAIL)
            startActivity(intent)
        }

        user_image.setOnClickListener(View.OnClickListener{

        })

        //사용자 정보 가져오기
        val user = auth!!.currentUser
        user?.let {
            val name = user.displayName // 이름
            val email = user.email // 이메일
            val displayName = user.displayName
            val photoUrl = user.photoUrl // 사용자 프로필
            val emailVerified = user.isEmailVerified // ..?
            val uid = user.uid

            user_name.setText(displayName)
            Glide.with(this).load(photoUrl).into(user_image)
        }

        var uAdapter : UserCustomAdapter

        //recyclerview
        val recyclerView_main = view.findViewById<RecyclerView>(R.id.recyceler_me_recruitment)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        recyclerView_main.layoutManager = staggeredGridLayoutManager

        val apiService = retrofit.create(RecruitmentAPIService::class.java)
        var apiCallForData = apiService.getUserRecruitment(USER_EMAIL)

        apiCallForData.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.d("에러 발생", t.message.toString())
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                val data = response.body() // 역직렬화를 통해 생성된 객체를 반환받음. RecruitmentAPIJSONResponseFromGSON타입의 객체임.

                if(data != null){

                    val gson = Gson()
                    var userList = arrayListOf<UserInfoData>();

                    for (i in 0 until data.size()){

                        val recruitmentData = gson.fromJson(data.get(i).toString(), RecruitmentDatas::class.java)

                        val r_no = recruitmentData.r_no
                        val title = recruitmentData.r_title.toString()
                        var content = recruitmentData.r_content?.toString()
                        var minPrice = ""
                        var email = recruitmentData.u_email
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

                        Log.d("날짜", date)
                        if(content.length >= 30){
                            content = content.substring(0,30)+"..."
                        }

                        userList.add(UserInfoData(r_no, email, title, minPrice, content, location, date))
                    }
                    uAdapter = UserCustomAdapter(activity as Context, userList)
                    recyclerView_main.adapter = uAdapter
                }
            }
        })

        return view
    }
}
