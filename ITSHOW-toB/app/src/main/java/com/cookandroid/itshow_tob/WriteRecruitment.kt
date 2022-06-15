package com.cookandroid.itshow_tob

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import com.cookandroid.itshow_tob.databinding.WriteRecruitmentBinding
import java.util.*
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.android.custom_dialog.CustomDialog
import com.android.custom_dialog.MultiChoiceModel
import com.android.custom_dialog.OnSelectItemInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class WriteRecruitment : AppCompatActivity() {
    //var : 가변, val : 불변
    private var rBinding: WriteRecruitmentBinding? = null
    private val binding get() = rBinding!!

    private var dateString = ""
    private var timeString = ""
    private val PICK_FROM_CAMERA = 0
    private val PICK_FROM_ALBUM = 1
    private val CROP_FROM_IMAGE = 2
    private val TAG = "DAHUIN_TAG"
    private lateinit var mImageCaptureUri:Uri
    private var imgPath = ""
    val multiChoiceAdapterList = ArrayList<MultiChoiceModel>(arrayListOf(
            MultiChoiceModel("음식", R.color.blue),
            MultiChoiceModel("편의", R.color.blue),
            MultiChoiceModel("생활", R.color.blue),
            MultiChoiceModel("의류", R.color.blue),
            MultiChoiceModel("도서", R.color.blue),
            MultiChoiceModel("문구", R.color.blue),
            MultiChoiceModel("잡화", R.color.blue),
            MultiChoiceModel("화장품", R.color.blue),
            MultiChoiceModel("쇼핑몰", R.color.blue),
            MultiChoiceModel("배달", R.color.blue),
            MultiChoiceModel("기타", R.color.blue)
    ))

    //이미지를 선택할 수 있도록 하는 함수
    //현재 사진 촬영은 보류
    private fun selectImg(){
        AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
//                .setPositiveButton("사진촬영", DialogInterface.OnClickListener{dialog, which ->
//
//                    //DB에 사진 저장하고 불러올 수 있도록 해야함
//                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    // 에러 --> 사진 저장하는부분인듯함.
//                    val url = "tmp_" + System.currentTimeMillis().toString() + ".jpg"
//                    //mImageCaptureUri = Uri.fromFile(File(Environment.getExternalStorageDirectory(), url)) // <--에서 아래와 같이 바꿈.(Android 7.0이상의 경우 앱 외부에 file://URI 의 노출을 금지하기 때문에)
//
//                    //이부분에서 안됨,,,
//                    val imagePath = File(this.filesDir, "images")
//                    val newFile = File(imagePath, url)
//                    mImageCaptureUri = FileProvider.getUriForFile(this, "com.cookandroid.itshow_tob", newFile)
//                    Log.d(TAG, mImageCaptureUri.toString())
//                    takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
//                    takePictureIntent.putExtra("return-data", true)
//                    takePictureIntent.resolveActivity(packageManager)?.also {
//                        //임시로 사용할 파일의 경로를 생성
//                        startActivityForResult(takePictureIntent, PICK_FROM_CAMERA)
//                    }
//
////                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////                        val url = "tmp_"+(System.currentTimeMillis()).toString() + ".jpg"
////                        val mImageCaptureUri = Uri.fromFile(File(Environment.getExternalStorageDirectory(), url))
////                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
////                        startActivityForResult(intent, PICK_FROM_CAMERA)
//                })
                .setNeutralButton("앨범선택", DialogInterface.OnClickListener{dialog, which ->
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    startActivityForResult(intent, PICK_FROM_ALBUM)
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener{dialog, which ->
                    dialog.dismiss()
                })
                .show()
    }

    // 권한 체크 함수 --> 카메라와 파일 공간의 권한을 체크하는 함수
    private fun cameraAndStorageAccess(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정이 완료되어 있습니다.")
                selectImg()
            } else {
                Log.d(TAG, "권한 설정을 요청합니다.")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    //카메라의 권한 설정과 관련된 코드 ---> 카메라와 파일 공간의 권한을 cameraAndStorageAccess()에서 요청을 하고, 요청에 대한 결과를 받을 수 있는 함수이다.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult")
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
            //권한을 획득 하였다.
            Log.d(TAG, "Permission: " + permissions[0] + " was " + grantResults[0])

            //이미지를 선택할 수 있도록 함.
            selectImg()
        }else{
            AlertDialog.Builder(this)
                    .setTitle("권한이 필요합니다.")
                    .setPositiveButton("확인", {dialog, which ->   })
                    .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun api_test(){

        //로딩창 객체 생성
        val loadingDialog = LoadingDialog(this)
        //투명하게
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textTitle = findViewById<EditText>(R.id.edit_title).text.toString()
        val textContent = findViewById<EditText>(R.id.edit_content).text.toString()
        var intMinAmount = findViewById<EditText>(R.id.edit_minAmount).text.toString().replace(",", "")
        val textOrder = findViewById<EditText>(R.id.edit_order).text.toString()
        val textLocation = findViewById<EditText>(R.id.edit_location).text.toString()
        val textCategory = findViewById<Button>(R.id.btn_category).text.toString()

        //올릴 수 있는지 확인. 조건을 만족하지 않으면 입력해달라고 하기.

        if(dateString=="" || timeString=="" || textTitle=="" || textOrder=="" || textLocation==""){
            AlertDialog.Builder(this)
                    .setTitle("필수 입력칸이 비어있습니다.")
                    .setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
                    .show()
            return
        }

        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDateTime.parse(dateString +" " + timeString, dtf)

        if(LocalDateTime.now().compareTo(date) > 0){
            //현재 시간보다 고른 시간이 더 적을 경우
            AlertDialog.Builder(this)
                    .setTitle("현재 시간보다 이전 시간을 선택하셨습니다.")
                    .setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> })
                    .show()
            return
        }

        //특정 사용자가 게시글을 올리는 코드 -- 이미지도 해야함
        val u_email = "s2003@e-mirim.hs.kr" //temp

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3003") //로컬호스트로 접속하기 위해!
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiService = retrofit.create(RecruitmentAPIService::class.java)

        loadingDialog.show()

        if(intMinAmount.equals("")){ intMinAmount = "0" }
        Log.d(TAG, (intMinAmount.toInt().toString()))
        val apiCallForData = apiService.createRecruitment(createRecruitmentDatas(u_email, textTitle,
                textContent, intMinAmount.toInt(), dateString +" "+timeString,
                textOrder, textLocation, textCategory, imgPath))

        apiCallForData.enqueue(object: Callback<createRecruitmentDatas>{
            override fun onFailure(call: Call<createRecruitmentDatas>, t: Throwable) {
                Log.d(TAG, "실패 ${t.message}")
                loadingDialog.dismiss()
                Toast.makeText(this@WriteRecruitment, "게시글 등록에 실패했습니다..!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<createRecruitmentDatas>, response: Response<createRecruitmentDatas>) {
                Log.d(TAG, "성공 ${response.raw()}")
                loadingDialog.dismiss()
                Toast.makeText(this@WriteRecruitment, "게시글이 등록되었습니다!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@WriteRecruitment, FrameMain::class.java)
                startActivity(intent)
            }

        })
        //게시글 올리는 코드 END


    }

    //이미지 저장 --> 카메라 혹은 파일 내의 이미지를 가져와 저장
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != RESULT_OK) {
            return
        }

        if(requestCode == PICK_FROM_ALBUM){
            mImageCaptureUri = data!!.data!! //!!는 null 값이 안 들어온다는 보증을 해주는 연산자.
            Log.d(TAG, mImageCaptureUri.path.toString())
        }
        if(requestCode == PICK_FROM_ALBUM || requestCode == PICK_FROM_CAMERA){
            //이미지를 가져온 후 리사이즈할 이미지 크기를 결정한다.
            //이후에 이미지 크롭 애플리케이션을 호출한다.
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(mImageCaptureUri, "image/*")

            //CROP할 이미지를 200 * 200크기로 저장
            intent.putExtra("crop", true)
            intent.putExtra("outputX", 200)
            intent.putExtra("outputY", 200)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)
            intent.putExtra("return-data", true)
            startActivityForResult(intent, CROP_FROM_IMAGE)
        }
        else if(requestCode == CROP_FROM_IMAGE) {
            //크롭이 된 이후의 이미지를 넘겨받음.
            //이미지뷰에 이미지를 보여주거나 부가적인 작업 이후에 임시 파일을 삭제한다.

            if(resultCode != RESULT_OK) {
                return
            }

            val extras: Bundle? = data!!.extras
            val filePath = Environment.getExternalStorageDirectory().absolutePath + "/ToB/" + System.currentTimeMillis() +".jpg"
            Log.d(TAG, filePath)
            imgPath = filePath
            if(extras != null){
                val photo = extras.getParcelable<Bitmap>("data")
                binding.recycelerView.adapter = WriteRecruitmentAdapter(photo)


            }
        } // if end
    }// fun end

    //날짜 선택 함수
    fun select_date(){
        val text_date = findViewById<TextView>(R.id.text_date)
        val cal = Calendar.getInstance()    //캘린더뷰 만들기

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${String.format("%04d", year)}-${String.format("%02d", month + 1)}-${String.format("%02d", dayOfMonth)}"
            text_date.text = dateString
        }

        val datePickerDialog = DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = cal.timeInMillis //현재 날짜 이전은 선택하지 못하도록
        datePickerDialog.show()
    }

    //시간 선택 함수
    fun select_time(){
        val text_time = findViewById<TextView>(R.id.text_time)
        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val currentTime = cal.timeInMillis

        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            timeString = "${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}"
            text_time.text = timeString
        }
        val timePickerDialog = TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true)
        timePickerDialog.show()
    }

    //카테고리 선택 함수
    fun select_category(){
        val btn_category = findViewById<Button>(R.id.btn_category)
        var category:String? = null

        val customDialog = CustomDialog(baseContext)
        customDialog.setTitle("카테고리 선택")

        customDialog.setMultiChoiceType(multiChoiceAdapterList as List<MultiChoiceModel>, OnSelectItemInterface {
            category = multiChoiceAdapterList.get(it).text
//            Toast.makeText(this, "선택된 항목 : "+category, Toast.LENGTH_LONG).show()
            btn_category.text = category?:"없음"
            customDialog.dismiss()
        })

        customDialog.show(supportFragmentManager, CustomDialog.TAG)

//        val btn_category = findViewById<Button>(R.id.btn_category)
//        var category:String? = null
//
//        val dialogView = layoutInflater.inflate(R.layout.select_category, null)
//
//
//
//        val builder = AlertDialog.Builder(this)
//        builder.setView(dialogView)
//        // res/values/strings.xml에 선언돼있습니다.
//        val items = resources.getStringArray(R.array.LAN)
//        val selectedItem = ArrayList<String>()
//        selectedItem.add(items[0])
//
//        builder.setTitle("카테고리 선택")
//
//        builder.setSingleChoiceItems(R.array.LAN, 0, DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
//            selectedItem.clear()
//            selectedItem.add(items[i])
//        })
//
//        builder.setPositiveButton("OK", DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
//            val category = selectedItem.get(0)
//            Toast.makeText(this, "선택된 항목 : "+category, Toast.LENGTH_LONG).show()
//            btn_category.text = category?:"없음"
//        })
//
//        builder.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflate는 xml에 표기된 레이아웃들을 메모리에 객체화시키는 행동
        //LayoutInflater는 xml에 표기된 레이아웃들을 실제 메모리에 올려주는 역할을 함.
        rBinding = WriteRecruitmentBinding.inflate(layoutInflater)

        //setContentView()메서드는 레이아웃을 출력하는 역할을 하는듯합니다.
        setContentView(binding.root)

        //이벤트 처리 관련 변수 선언
        val img_writeBack = findViewById<ImageView>(R.id.img_writeBack)
        val btn_imgUpLoad = findViewById<Button>(R.id.btn_imgUpload)
        val text_date = findViewById<TextView>(R.id.text_date)
        val text_time = findViewById<TextView>(R.id.text_time)
        val btn_submit = findViewById<Button>(R.id.btn_submit)
        val btn_category = findViewById<Button>(R.id.btn_category)
        val edit_minAmount = findViewById<EditText>(R.id.edit_minAmount)

        //최소모집가격의 콤마를 찍도록 설정하는 코드
        edit_minAmount.addTextChangedListener(object:TextWatcher{
            val editText = edit_minAmount
            var strAmount = ""

            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(strAmount)){
                    strAmount = makeStringComma(s.toString().replace(",", ""))
                    editText.setText(strAmount)
                    val editable = editText.text
                    Selection.setSelection(editable, strAmount.length)
                }
            }
            protected fun makeStringComma(str:String):String{
                if(str.length == 0){ return "" }
                val format = DecimalFormat("###,###")
                return format.format(str.toLong())
            }
        })

        //모집기간
        text_date.setOnClickListener {
            select_date()
        }

        //모집시간
        text_time.setOnClickListener {
            select_time()
        }

        //이미지 업로드 버튼 클릭 이벤트
        btn_imgUpLoad.setOnClickListener{
            //버튼을 누르면 이미지 선택하도록 해야함
            //https://jeongchul.tistory.com/287
            cameraAndStorageAccess()
        }
        //이미지 업로드 버튼 클릭 end

        // 뒤로가기 버튼 (메인)
        img_writeBack.setOnClickListener {
            //Intent는 액티비티, 서비스, 브로드캐스트 리시ㄴ버, 컨텐트 프로바이더
            //각각의 컴포넌트간의 통신을 맡고 있다고 한다.
            //아래는 액티비티를 전환하는 것이다.
            finish()
        }

        //등록 버튼을 눌렀을시
        btn_submit.setOnClickListener{
            api_test()
        }

        //카테고리 선택 버튼
        btn_category.setOnClickListener{
            select_category()
        }

    }
}
