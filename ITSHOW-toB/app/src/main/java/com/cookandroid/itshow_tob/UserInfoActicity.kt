package com.cookandroid.itshow_tob

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.willy.ratingbar.BaseRatingBar
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response

class UserInfoActicity : AppCompatActivity() {
    var u_email = "" //이 사람의 이메일 저장
    var emailWithFollow = ""
    var followWithFollow = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_info_acticity)

        val intent = getIntent()
        //누구꺼인지 값 가져오기
        val u_name = intent.getStringExtra("u_name")
        emailWithFollow = intent.getStringExtra("preEmail").toString()
        followWithFollow = intent.getStringExtra("preFollow").toString()
        Log.d(TAG, u_name.toString())

        var user_name: TextView = findViewById(R.id.user_info_name)
        var user_image: ImageView = findViewById(R.id.user_info_Image)
        val user_rating = findViewById<BaseRatingBar>(R.id.ratingbar_user)
        val img_UserInfogBack = findViewById<ImageView>(R.id.img_UserInfogBack)
        val btn_follow = findViewById<Button>(R.id.btn_follow)
        val layout_follower_info = findViewById<LinearLayout>(R.id.layout_follower_info)
        val layout_following_info = findViewById<LinearLayout>(R.id.layout_following_info)

        val loadingDialog = LoadingDialog(this)//로딩창 객체 생성
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//투명하게

        if(u_name!=null) {
            //유저의 정보를 가져와서 세팅
            val apiService_user = retrofit.create(UserAPIService::class.java)
            val apiCallForData_userInfo = apiService_user.getUserInfoWithName(u_name)
            loadingDialog.show()

            apiCallForData_userInfo.enqueue(object : Callback<UserInfoDatas>{
                override fun onResponse(
                        call: retrofit2.Call<UserInfoDatas>, response: Response<UserInfoDatas>
                ) {
                    loadingDialog.dismiss()
                    val data = response.body()
                    Log.d(TAG, data.toString())
                    user_name.text = data?.u_name
                    user_rating.rating = (data?.u_star)?.toFloat()?:0.0f
                    u_email = data?.u_email.toString()
                    Glide.with(this@UserInfoActicity).load(data?.u_img).into(user_image)

                    //정보 가져온 후 팔로우중인지 확인
                    if(!u_email.equals("")) {
                        val apiService_follow = retrofit.create(FlowAPIService::class.java)
                        val apiCallForData_followWether = apiService_follow.followWether( USER_EMAIL, u_email )//USER_EMAIL은 나의 이메일, u_email은 상대방 이메일
                        loadingDialog.show()

                        apiCallForData_followWether.enqueue(object : Callback<ResponseBody>{
                            override fun onResponse(
                                    call: retrofit2.Call<ResponseBody>, response: Response<ResponseBody>
                            ) {
                                loadingDialog.dismiss()

                                val followStr = response.body()?.string().toString()
                                Log.d(TAG, "팔로우중? : "+ followStr)
                                if(followStr.equals("false")){
                                    btn_follow.setText("팔로우")
                                    btn_follow.setTextColor(Color.parseColor("#FFFFFF"))
                                    btn_follow.setBackgroundResource(R.drawable.button_circle1_light_blue)
                                }else{
                                    //팔로우중임
                                    btn_follow.setText("언팔로우")
                                    btn_follow.setTextColor(Color.parseColor("#000000"))
                                    btn_follow.setBackgroundResource(R.drawable.button_round1_white_gray)
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                                loadingDialog.dismiss()
                                Toast.makeText(this@UserInfoActicity, "팔로우 정보를 불러오지 못했습니다.", Toast.LENGTH_LONG).show()
                                finish()
                            }

                        })
                    }//if end
                }//f end
                override fun onFailure(call: retrofit2.Call<UserInfoDatas>, t: Throwable) {
                    loadingDialog.dismiss()
                    Toast.makeText(this@UserInfoActicity, "유저 정보를 불러오지 못했습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }
            })


        }else{
            //이 유저가 없다.
            finish()
        }

        btn_follow.setOnClickListener{
            if(u_email==null) return@setOnClickListener
            val apiService_follow = retrofit.create(FlowAPIService::class.java)
            var apiCallForData = apiService_follow.unFollow(USER_EMAIL, u_email)
            loadingDialog.show()
            if(btn_follow.text.equals("언팔로우")){
                btn_follow.setText("팔로우")
                btn_follow.setTextColor(Color.parseColor("#FFFFFF"))
                btn_follow.setBackgroundResource(R.drawable.button_circle1_light_blue)
            }else{
                Log.d(TAG, "팔로우하기")
                //팔로우중임
                btn_follow.setText("언팔로우")
                btn_follow.setTextColor(Color.parseColor("#000000"))
                btn_follow.setBackgroundResource(R.drawable.button_round1_white_gray)
                //서버 설정은 위에서 함
                apiCallForData = apiService_follow.newFollow(FollowData(USER_EMAIL, u_email))
            }

            apiCallForData.enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: Response<ResponseBody>
                ) {
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    loadingDialog.dismiss()
                    Toast.makeText(this@UserInfoActicity, "팔로우 정보를 불러오지 못했습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }

            })

        }


        img_UserInfogBack.setOnClickListener{
            back()
            finish()
        }

        layout_follower_info.setOnClickListener{
            if(u_email==null) return@setOnClickListener
            val intent = Intent(this@UserInfoActicity, FollowList::class.java)
            intent.putExtra("follow", "follower")
            intent.putExtra("u_email", u_email)
            startActivity(intent)
        }

        layout_following_info.setOnClickListener{
            if(u_email==null) return@setOnClickListener
            val intent = Intent(this@UserInfoActicity, FollowList::class.java)
            intent.putExtra("follow", "following")
            intent.putExtra("u_email", u_email)
            startActivity(intent)
        }

        user_image.setOnClickListener(View.OnClickListener{

        })
    }

    fun back(){
        //비어있으면 전에 누구의 리스트를 보지 않음. 현재 프로필의 주인의 리스트가 아니어야함
        val intent = Intent(this@UserInfoActicity, FollowList::class.java)
        if(!followWithFollow.equals("") && !emailWithFollow.equals("")) {
            //현재 프로필 주인의 리스트이면
            intent.putExtra("follow", followWithFollow)
            intent.putExtra("u_email", emailWithFollow)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        back()
        super.onBackPressed()
    }

}