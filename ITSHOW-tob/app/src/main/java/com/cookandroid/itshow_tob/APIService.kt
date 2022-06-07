package com.cookandroid.itshow_tob

import com.google.gson.JsonArray
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

//게시글 정보를 요청하기 위한 인터페이스
interface RecruitmentAPIService {

    //게시글 가저오기
    @GET("/recruitment")
    fun getRecruitmentInfo(
        @Query("r_no") r_no:Int
    ):Call<JsonArray>

    //게시글 생성
    @POST("/recruitment/new")
    fun createRecruitment(
            @Body recruitmentItem:createRecruitmentDatas
    ):Call<createRecruitmentDatas>
}

//유저 관련 api요청
interface UserAPIService {
    //유저이름으로 유저 정보 조회
    @GET("/users")
    fun getUserInfoWithName(
            @Query("u_name") u_name:String
    ):Call<UserInfoDatas>

    //유저 이름 변경
    @PUT("/users/{u_id}/renameUser")
    fun renameUser(
            @Path("u_id") u_id:String,
            @Body u_name:String //변경할 닉네임
    ): Call<ResponseBody>

    //유저 정보 모두 삭제
    @DELETE("users/{u_id}/deleteUser")
    fun deleteUser(@Path("u_id") u_id:String) : Call<ResponseBody>
}

data class UserInfoDatas(val u_id:String, val u_name:String, val u_star:Double)

//게시글 정보를 담을곳. Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
data class RecruitmentDatas(val u_id:String, val r_title : String, val r_content: String, val r_minPrice: Int, val r_inprogress:Int,
                                              val r_startDate: String, val r_endDate : String, val r_order:String, val r_location:String,
                                              val r_category:String, val r_imgPath: String)

data class createRecruitmentDatas(val u_id:String, val r_title : String, val r_content: String, val r_minPrice: Int,val r_endDate : String,
                                  val r_order:String, val r_location:String,
                            val r_category:String?, val r_imgPath: String)