package com.cookandroid.itshow_tob

import com.google.gson.JsonArray
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3003") //로컬호스트로 접속하기 위해!
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//게시글 정보를 요청하기 위한 인터페이스
interface RecruitmentAPIService {

    //전체 게시글 가저오기
    @GET("/recruitment-list")
    fun getRecruitmentList(
    ):Call<JsonArray>

    // 나의 게시글 가저오기
    @GET("/recruitment")
    fun getRecruitmentInfo(
        @Query("r_no") r_no:Int
    ):Call<JsonArray>

    //게시글 생성
    @POST("/recruitment/new")
    fun createRecruitment(
            @Body recruitmentItem:createRecruitmentDatas
    ):Call<createRecruitmentDatas>

    //카테고리별 게시글 가져오기
    @POST("/recruitment-category")
    fun getCategoryRecruitment(
            @Query("category") category:String
    ):Call<JsonArray>
}

//유저 관련 api요청
interface UserAPIService {

    @POST("/users/new")
    fun addUser(
            @Query("u_email") u_email:String,
            @Query("u_name") u_name:String,
            @Query("u_img") u_img:String
    ):Call<JsonArray>

    //유저이름으로 유저 정보 조회
    @GET("/users")
    fun getUserInfoWithName(
            @Query("u_name") u_name:String
    ):Call<UserInfoDatas>

    //유저 이름 변경
    @PUT("/users/{u_email}/renameUser")
    fun renameUser(
            @Path("u_email") u_email:String,
            @Body u_name:UserNameData //변경할 닉네임
    ): Call<ResponseBody>

    //유저 정보 모두 삭제
    @DELETE("users/{u_email}/deleteUser")
    fun deleteUser(@Path("u_email") u_email:String) : Call<ResponseBody>
}

interface SearchAPIService{

    @GET("/users/{u_email}/recent-search")
    fun searchRecent(@Path("u_email") u_email:String):Call<JsonArray>

    @GET("/search/popular")
    fun searchPopular():Call<JsonArray>

    //검색어 저장
    @POST("/search/new")
    fun newSearchWord(
            @Body searchWord:CreateSearchWord
    ):Call<ResponseBody>

    //게시글 검색
    @POST("/recruitment-search")
    fun recruitmentSearch(
            @Query("search_word") search_word:String
    ):Call<JsonArray>
}

interface FlowAPIService{

    //어떤 사람의 팔로잉 목록을 가져옴
    @GET("/users/{u_email}/followings")
    fun flowingListOfUser(
            @Path("u_email") u_email:String
    ):Call<JsonArray>

    //어떤 사람의 팔로워 목록을 가져옴
    @GET("/users/{following}/followers")
    fun followerListOfUser(
            @Path("following") following:String
    ):Call<JsonArray>

    //어떤 사람이 이 사람을 팔로우하고있는지의 여부를 true, false로 리턴함.
    @GET("/follow/whether")
    fun followWether(
            @Query("u_email") u_email:String,
            @Query("following") following:String
    ):Call<ResponseBody>

    //팔로우하기
    @POST("/follow/new")
    fun newFlow(
            @Body searchWord: FollowData
    ):Call<ResponseBody>

    //언팔로우하기
    @POST("/follow/delete")
    fun unFlow(
            @Body searchWord: FollowData
    ):Call<ResponseBody>

}

data class SearchWordData(val s_word:String)

data class FollowData(val u_email: String, val flowing: String)

data class CreateSearchWord(val u_email:String, val s_word:String)

data class UserInfoDatas(val u_email:String, val u_name:String, val u_star:Double, val u_img: String)

data class UserNameData(val u_name:String)

//게시글 정보를 담을곳. Gson은 객체를 JSON 표현으로 변환하는 데 사용할 수 있는 라이브러리인듯하다.
data class RecruitmentDatas(val r_no:Int, val u_email: String, val r_title : String, val r_content: String, val r_minPrice: Int, val r_inprogress:Int,
                                              val r_startDate: String, val r_endDate : String, val r_order:String, val r_location:String,
                                              val r_category:String, val r_imgPath: String)

data class createRecruitmentDatas(val u_email:String, val r_title : String, val r_content: String, val r_minPrice: Int,val r_endDate : String,
                                  val r_order:String, val r_location:String,
                            val r_category:String?, val r_imgPath: String)