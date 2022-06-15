package com.cookandroid.itshow_tob


import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Login : AppCompatActivity() {
    // firebase 인증을 위한 변수
    var auth : FirebaseAuth ? = null

    // 구글 로그인 연동에 필요한 변수
    var googleSignInClient : GoogleSignInClient ? = null
    var GOOGLE_LOGIN_CODE = 9001


    // onCreate는 Acitivity가 처음 실행 되는 상태에 제일 먼저 호출되는 메소드로 여기에 실행시 필요한 각종 초기화 작업을 적어줌
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인 페이지가 첫화면임 (activity_main.xml의 레이아웃 사용)
        setContentView(R.layout.login)

        // firebaseauth를 사용하기 위한 인스턴스 get
        auth = FirebaseAuth.getInstance()

        // xml에서 구글 로그인 버튼 코드 가져오기
        var google_sign_in_button = findViewById<SignInButton>(R.id.btn_googleSignIn)

        // 구글 로그인 버튼 클릭 시 이벤트 : googleLogin function 실행
        google_sign_in_button.setOnClickListener {
            googleLogin()
        }

        // 구글 로그인을 위해 구성되어야 하는 코드 (Id, Email request)
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("611009617491-76ved3mmhklfcbbh1d73j11bfbjpu68q.apps.googleusercontent.com")
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 사용자 정보 가져오기
        val user = auth!!.currentUser
        user?.let{
            val name = user.displayName
            val email = user.email
            val displayName = user.displayName
            val photoUrl = user.photoUrl
            val emailVerified = user.isEmailVerified

            val uid = user.uid
            Toast.makeText(this,  name.toString(), Toast.LENGTH_LONG).show()
            Log.d(TAG, "handleSignInResult:personEmail $photoUrl")
        }

    } // onCreate

    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    } // googleLogin

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)//이부분에서 문제가 있는듯
            if (result != null) {
                if(result.isSuccess) {
                    var account = result.signInAccount
                    firebaseAuthWithGoogle(account)
                }
            }
        } //if
    } // onActivityResult

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                    task ->
                    if(task.isSuccessful) {

                        // 로그인 성공 시
                        /*
                        @RequiresApi(Build.VERSION_CODES.O)
                        fun UserAPI(){

                            //로딩창 객체 생성
                            val loadingDialog = LoadingDialog(this)
                            //투명하게
                            loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            val retrofit = Retrofit.Builder()
                                .baseUrl("http://10.0.2.2:3000") //로컬호스트로 접속하기 위해!
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()

                            val apiService = retrofit.create(UserAPIService::class.java)
                            loadingDialog.show()

                            var name:String = "";
                            var email:String = "";

                            val user = auth!!.currentUser
                            user?.let {
                                name = user.displayName.toString()
                                email = user.email.toString()
                                Log.d(TAG, name)
                            }

                            val apiCallForData = apiService.addUser(UserInfoDatas(email, name, u_star = 0.0))

                            apiCallForData.enqueue(object: Callback<UserInfoDatas>{
                                override fun onFailure(call: Call<UserInfoDatas>, t: Throwable) {
                                    Log.d(TAG, "실패 ${t.message}")
                                    loadingDialog.dismiss()
                                    Toast.makeText(this@Login, "유저가 저장되지 않았습니다", Toast.LENGTH_LONG).show()
                                }

                                override fun onResponse(call: Call<UserInfoDatas>, response: Response<UserInfoDatas>) {
                                    Log.d(TAG, "성공 ${response.raw()}")
                                    loadingDialog.dismiss()
                                    Toast.makeText(this@Login, "유저가 저장되었습니다", Toast.LENGTH_LONG).show()
                                }

                            })

                        }
                        UserAPI()

                         */

                        // Toast.makeText(this,  "success", Toast.LENGTH_LONG).show()
                        startActivity(Intent (this, FrameMain::class.java))

                    } else {
                        Toast.makeText(this,  task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
    } //firebaseAuthWithGoogle



}

