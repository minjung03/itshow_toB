package com.cookandroid.itshow_tob

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Setting : AppCompatActivity() {
    val TAG = "dahuin"
    var message : String = ""
    var auth : FirebaseAuth? = null

    private var mAuth: FirebaseAuth? = null

    private val RC_SIGN_IN = 9001
/*
    //회원의 정보를 모두 삭제하는 코드
    fun delete_userInfo(apiService: UserAPIService):String{
        //로딩창 객체 생성
        val loadingDialog = LoadingDialog(this)
        //투명하게
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //정보 삭제 수행
        val u_email = "s2007@e-mirim.hs.kr" //temp. 항상 저장된 아이디를 가져와야함.
        var message = "계정 정보 삭제에 실패했습니다."
        //로그인이 돼있을경우
        if(!u_email.isBlank()) {
            //특정 사용자의 모든 정보를 삭제하는 코드
            val apiCallForData = apiService.deleteUser(u_email)
            apiCallForData.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "실패 ${t.message}")
                    loadingDialog.dismiss()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d(TAG, "성공 ${response.raw()}")
                    message = "모든 정보가 삭제되었습니다."
                    loadingDialog.dismiss()
                    Log.d(TAG, "성공 $message")
                }
            })
        }
        else {
            message = "로그인 되어있지 않습니다."
        }
        Log.d(TAG, "성공 $message")
        return message
    }
*/


    //회원의 이름을 바꾸는 코드
    fun rename_user(apiService: UserAPIService, infoDialog: AlertDialog, textContent_info:TextView, editNameDialog:AlertDialog, editNameView:View){
        //로딩창 객체 생성
        val loadingDialog = LoadingDialog(this)
        //투명하게
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        //로딩중
        /*
        * https://daldalhanstory.tistory.com/189
        *https://jinsangjin.tistory.com/45
        * */

        //정보 삭제 수행
        val u_email = "s2003@e-mirim.hs.kr" //temp
        var userAlreadyExists = false
        val editTextNickname = editNameView.findViewById<EditText>(R.id.editNickname)

        val textNickname = editTextNickname.text.toString()

        //로그인이 돼있을경우
        if(!u_email.isBlank()) {
            //닉네임이 존재하는지 확인
            val apiCallForData = apiService.getUserInfoWithName(textNickname)

            loadingDialog.show()

            apiCallForData.enqueue(object : Callback<UserInfoDatas> {
                override fun onFailure(call: Call<UserInfoDatas>, t: Throwable) {
                    Log.d(TAG, "1실패 ${t.message}")
                    //유저가 존재하지 않음
                    userAlreadyExists = false
                    loadingDialog.dismiss()
                }
                override fun onResponse(call: Call<UserInfoDatas>, response: Response<UserInfoDatas>) {
                    Log.d(TAG, "성공 ${response.raw()}")
                    if(response.body() != null){
                        //유저가 존재한다.
                        userAlreadyExists = true
                        loadingDialog.dismiss()
                    }
                }
            })

            Log.d(TAG, "textNickname:"+textNickname)
            if(userAlreadyExists) {
                textContent_info.text = "닉네임이 이미 존재합니다."
                infoDialog.show()
            }
            else if (textNickname.isBlank() || textNickname.contains(" ")) {
                //공백 포함 여부
                textContent_info.text = "닉네임에는 공백이 들어갈 수 없습니다."
                infoDialog.show()
            } else if (textNickname.length < 2) {
                textContent_info.text = "닉네임은 최소 2글자 이상입니다."
                infoDialog.show()
            } else {
                val apiCallForData = apiService.renameUser(u_email, UserNameData(textNickname))
                loadingDialog.show()
                apiCallForData.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "실패 ${t.message}")
                        loadingDialog.dismiss()

                        textContent_info.text = "닉네임 변경에 실패했습니다."
                        infoDialog.show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d(TAG, "성공 ${response.raw()}")
                        loadingDialog.dismiss()

                        if(response.raw().code() == 200) {
                            textContent_info.text = "닉네임이 " + textNickname + "로 변경되었습니다."
                            editNameDialog.dismiss()
                            infoDialog.show()
                        }else if(response.raw().code() == 400){
                            textContent_info.text = "닉네임 변경에 실패했습니다."
                            infoDialog.show()
                        }
                    }
                })


            }
        }
        else {
            textContent_info.text = "로그인 되어있지 않습니다."
            infoDialog.show()
        }

    }

    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        mAuth = FirebaseAuth.getInstance();

        val img_SettingBack = findViewById<ImageView>(R.id.img_SettingBack)
        val switch_appPush= findViewById<Switch>(R.id.switch_appPush)
        val btn_name_edit = findViewById<Button>(R.id.btn_name_edit)
        val btn_Help = findViewById<Button>(R.id.btn_Help)
        val btn_UserDelete= findViewById<Button>(R.id.btn_UserDelete)
        val btn_logout = findViewById<Button>(R.id.btn_logout)

        // 뒤로가기 버튼 (내 프로필 화면으로 이동)
        img_SettingBack.setOnClickListener(View.OnClickListener {
            finish()

        })

        //알림은 일단 보류
        switch_appPush.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // 앱 알림 동의
            } else {
                // 앱 알림 비동의
            }
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("611009617491-76ved3mmhklfcbbh1d73j11bfbjpu68q.apps.googleusercontent.com")
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btn_logout.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient?.signOut()?.addOnCompleteListener {
                this.finish()
            }
        })

        //정보창
        val infoView = layoutInflater.inflate(R.layout.dialog_information, null)
        val textContent_info = infoView.findViewById<TextView>(R.id.textContent)
        val btnOk_info = infoView.findViewById<TextView>(R.id.btnOk)
        val infoDialog = AlertDialog.Builder(this).setView(infoView).create()
        btnOk_info.setOnClickListener{infoDialog.dismiss()}

        //apiService 생성
        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3003") //로컬호스트로 접속하기 위해!
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiService = retrofit.create(UserAPIService::class.java)

        //닉네임 변경
        val editNameView = layoutInflater.inflate(R.layout.dialog_edit_nickname, null)
        val editTextNickname = editNameView.findViewById<EditText>(R.id.editNickname)
        val textCurNicname = editNameView.findViewById<TextView>(R.id.textCurNicname)
        val btnChange = editNameView.findViewById<Button>(R.id.btnChange)
        val editNameDialog = AlertDialog.Builder(this).setView(editNameView).create()

        btn_name_edit.setOnClickListener{
            textCurNicname.text = "김다흰babu" //현재 닉네임
            editTextNickname.setText("")
            editNameDialog.show()
        }
        btnChange.setOnClickListener{
            //닉네임 변경 버튼을 눌렀을때
            rename_user(apiService, infoDialog, textContent_info, editNameDialog, editNameView)
        }

        //도움말
        btn_Help.setOnClickListener{
            val intent = Intent(applicationContext, HelpContent::class.java)
            startActivity(intent)
        }

        //계정 정보 모두 삭제
        val  editDeleteUserView = layoutInflater.inflate(R.layout.dialog_confirm, null)
        val textTitle = editDeleteUserView.findViewById<TextView>(R.id.textTitle)
        val textContent = editDeleteUserView.findViewById<TextView>(R.id.textContent)
        val btnOk = editDeleteUserView.findViewById<Button>(R.id.btnOk)
        val btnNo = editDeleteUserView.findViewById<Button>(R.id.btnNo)
        val deleteUserDialog = AlertDialog.Builder(this).setView(editDeleteUserView).create()
        textTitle.text = "계정 정보 모두 삭제"
        textContent.text = "계정 정보를 삭제하면 게시글, 팔로우, 등 회원님과 관련된 모든 정보가 삭제됩니다. 정말 삭제하시겠습니까?"
        btn_UserDelete.setOnClickListener{
            deleteUserDialog.show()
        }
        btnOk.setOnClickListener {
            deleteUserDialog.dismiss()

            //정보 삭제 수행
            val u_email = "s2003@e-mirim.hs.kr" //temp. 항상 저장된 아이디를 가져와야함.

            //특정 사용자의 모든 정보를 삭제하는 코드
            val apiCallForData = apiService.deleteUser(u_email)

            apiCallForData.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "실패 ${t.message}")
                    textContent_info.text = "삭제 실패" //정보창에 띄울 메시지를 가져온다.
                    infoDialog.show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    textContent_info.text = "모든 정보가 삭제되었습니다." //정보창에 띄울 메시지를 가져온다.
                    infoDialog.show()
                    Log.d(TAG, "안녕"+response.body()?.string())
                    Log.d(TAG, "성공 ${response.raw()}")
                }
            })
            textContent_info.text = "삭제 실패" //정보창에 띄울 메시지를 가져온다.
            infoDialog.show()
        }


        btnNo.setOnClickListener{ deleteUserDialog.dismiss() }
    }

}