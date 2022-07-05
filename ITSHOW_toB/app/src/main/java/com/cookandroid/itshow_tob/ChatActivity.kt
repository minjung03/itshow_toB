package com.cookandroid.itshow_tob

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.itshow_tob.databinding.FragmentChatBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class ChatActivity: AppCompatActivity() {

    val db = Firebase.firestore
    var nick: String = ""
    var contexts: String = ""
    var times: String = ""
    var rooms: String = ""
    var myname: String = ""
    var chatData = arrayListOf<ChatData>()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: String            // 현재 닉네임
    private lateinit var registration: ListenerRegistration    // 문서 수신
    private lateinit var chadapter: ChatAdapter   // 리사이클러 뷰 어댑터
    var auth: FirebaseAuth? = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = FragmentChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val r_no = intent.getStringExtra("r_no").toString()
        Log.d("ToB", r_no)

        val rv_list = findViewById<RecyclerView>(R.id.rv_list)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_list.setLayoutManager(linearLayoutManager)


        var chat_back = findViewById<ImageView>(R.id.chat_back)
        chat_back.setOnClickListener(View.OnClickListener {
            finish()
        })

        // 사용자가 들어가 있는 방 번호 얻기
        val user = auth!!.currentUser
        user?.let {
            myname = user.displayName.toString()

            db.collection("Chat")
                .whereEqualTo("room", r_no).orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        nick = document.data.getValue("nickname").toString()
                        contexts = document.data.getValue("contents").toString()

                        val timestamp2 = document.data.getValue("time") as Timestamp
                        val sf2 = SimpleDateFormat("MM/dd HH:mm", Locale.KOREA)
                        sf2.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                        val time = sf2.format(timestamp2.toDate())

                        rooms = document.data.getValue("room") as String

                        Log.d("tag : ", "${nick + contexts + time + rooms} ")

                        if(contexts == "") continue;
                        else
                        chatData.add(ChatData(nick, contexts, time, rooms))

                    }
                    chadapter = ChatAdapter(myname, chatData)
                    rv_list.adapter = chadapter
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "----------Error getting documents.", exception)
                }
            // 얻은 방번호 리스트를 이용해 for문 돌리기

        }
        // 입력 버튼
        binding.btnSend.setOnClickListener {
            var dateFormat = SimpleDateFormat("yy-MM-dd HH:mm")
            val datetime: LocalDateTime = LocalDateTime.now()
            val format = DateTimeFormatter.ofPattern("yy-MM-dd HH:ss")

            val currentMillis = System.currentTimeMillis()
            val currentDateTime =
                Instant.ofEpochMilli(currentMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()

            // 채팅창이 공백일 경우 버튼 비활성화
            if (binding.etChatting.text.toString() == "") {
                Toast.makeText(this, "메세지를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                // 입력 데이터

                val data = hashMapOf(
                    "nickname" to myname,
                    "contents" to binding.etChatting.text.toString(),
                    "time" to Timestamp.now(),
                    "room" to r_no
                    //채팅 번호
                )
                // Firestore에 기록
                db.collection("Chat").add(data)
                    .addOnSuccessListener {
                        binding.etChatting.text.clear()
                        Log.w("ChatFragment", "Document added: $it")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "전송하는데 실패했습니다", Toast.LENGTH_SHORT).show()
                        Log.w("ChatFragment", "Error occurs: $e")

                    }
            }
        }

        val enterTime = Date(System.currentTimeMillis())

        registration = db.collection("Chat")
            .whereEqualTo("room", r_no).orderBy("time", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshots, e ->
                // 오류 발생 시
                if (e != null) {
                    Log.w("ChatFragment", "Listen failed: $e")
                    return@addSnapshotListener
                }

                // 원하지 않는 문서 무시
                if (snapshots!!.metadata.isFromCache) return@addSnapshotListener

                // 문서 수신
                for (doc in snapshots.documentChanges) {
                    val timestamp = doc.document["time"] as Timestamp

                    // 문서가 추가될 경우 리사이클러 뷰에 추가
                    if (doc.type == DocumentChange.Type.ADDED && timestamp.toDate() > enterTime) {
                        val nickname = doc.document["nickname"].toString()
                        val contents = doc.document["contents"].toString()

                        // 타임스탬프를 한국 시간, 문자열로 바꿈
                        val sf = SimpleDateFormat("MM/dd HH:mm", Locale.KOREA)
                        sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                        val time = sf.format(timestamp.toDate())
                        val room = r_no

                        val item = ChatData(nickname, contents, time, r_no)
                        chatData.add(item)
                    }
//                    chadapter.notifyDataSetChanged()
                }
            }

        //나가기 버튼
        binding.imgExit.setOnClickListener {
            db.collection("Chat").whereEqualTo("nickname", myname).whereEqualTo("room", r_no)
                    .get()
                    .addOnSuccessListener { result ->

                        for (document in result) {
                            document.reference.update("nickname", "알 수 없음")
                        }
                        onBackPressed()
                    }
        }


        //계정 정보 모두 삭제
        val  viewMap = layoutInflater.inflate(R.layout.dialog_map, null)
        val btnOk = viewMap.findViewById<Button>(R.id.btnOk)
        val viewMapDialog = AlertDialog.Builder(this).setView(viewMap).create()
        binding.imgViewMap.setOnClickListener{
            viewMapDialog.show();
            viewMapDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        }
        btnOk.setOnClickListener{
            viewMapDialog.dismiss()
        }

        /*
        //거래완료 버튼
        binding.chatComplet.setOnClickListener {

            val  deleteCheatView = layoutInflater.inflate(R.layout.dialog_confirm, null)
            val textContent = deleteCheatView.findViewById<TextView>(R.id.textContent)
            val btnOk = deleteCheatView.findViewById<Button>(R.id.btnOk)
            val btnNo = deleteCheatView.findViewById<Button>(R.id.btnNo)

            textContent.text = "지금까지의 채팅 내용이\n전부 삭제됩니다\n정말 거래완료를 하시겠습니까?"
            val deleteCheatDialog = AlertDialog.Builder(this).setView(deleteCheatView).create()
            deleteCheatDialog.show()
            deleteCheatDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            btnOk.setOnClickListener{
                db.collection("Chat").whereEqualTo("room", r_no)
                        .get()
                        .addOnSuccessListener { result ->

                            for (document in result) {
                                document.reference.delete()
                            }
                            onBackPressed()
                        }
            }

            btnNo.setOnClickListener{
                deleteCheatDialog.dismiss()
            }


        }
         */
        binding.chatBack.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ChatActivity, FrameMain::class.java)
        startActivity(intent)
        finish()
    }
}
