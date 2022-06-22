package com.cookandroid.itshow_tob

<<<<<<< Updated upstream
=======
import android.annotation.SuppressLint
>>>>>>> Stashed changes
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
<<<<<<< Updated upstream
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.cookandroid.itshow_tob.databinding.FragmentChatBinding
=======
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.itshow_tob.databinding.ActivityChatBinding
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    private var _binding: FragmentChatBinding? = null
=======
    private var _binding: ActivityChatBinding? = null
>>>>>>> Stashed changes
    private val binding get() = _binding!!
    private lateinit var currentUser: String            // 현재 닉네임
    private lateinit var registration: ListenerRegistration    // 문서 수신
    private lateinit var chadapter: ChatAdapter   // 리사이클러 뷰 어댑터
    var auth: FirebaseAuth? = FirebaseAuth.getInstance()

<<<<<<< Updated upstream
=======
    @SuppressLint("WrongViewCast")
>>>>>>> Stashed changes
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

<<<<<<< Updated upstream
        _binding = FragmentChatBinding.inflate(layoutInflater)
=======
        _binding = ActivityChatBinding.inflate(layoutInflater)
>>>>>>> Stashed changes
        setContentView(binding.root)

        val intent = getIntent()
        val r_no = intent.getStringExtra("r_no").toString()
        Log.d("ToB", r_no)

        val rv_list = findViewById<RecyclerView>(R.id.rv_list)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_list.setLayoutManager(linearLayoutManager)

<<<<<<< Updated upstream
        //지도 보기
        val text_view_map = findViewById<TextView>(R.id.text_view_map)
        text_view_map.setOnClickListener{
            val imagePopup = ImagePopup(this)
            imagePopup.initiatePopup(getDrawable(R.drawable.view_map))
            imagePopup.viewPopup()
        }

=======
        //뒤로가기
        val btn_chatRoomBack = findViewById<Button>(R.id.btn_chatRoomBack)
        btn_chatRoomBack.setOnClickListener{
            finish()
        }
        
        
>>>>>>> Stashed changes
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

    }



//
//    override fun onDestroy() {
//        super.onDestroy()
//        if(registration!=null) {
//            registration.remove()
//        }
//        _binding = null
//    }

}





/*
class ChatFragment: Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: String            // 현재 닉네임
    private val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스
    private lateinit var registration: ListenerRegistration    // 문서 수신
    private val chatList = arrayListOf<ChatData>()    // 리사이클러 뷰 목록
    private lateinit var adapter: ChatAdapter   // 리사이클러 뷰 어댑터
    var auth : FirebaseAuth? = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // LoginFragment 에서 입력한 닉네임을 가져옴


        val user = auth!!.currentUser
        user?.let{
            val displayName  = user.displayName

            currentUser = displayName.toString();

            //Toast.makeText(this,  name.toString(), Toast.LENGTH_LONG).show()
            //Log.d(ContentValues.TAG, "handleSignInResult:personEmail $photoUrl")
        }

      */
/*  arguments?.let {
            currentUser = it.getString("nickname").toString()
        }*//*

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        Toast.makeText(context, "현재 닉네임은 ${currentUser}입니다.", Toast.LENGTH_SHORT).show()





        // 리사이클러 뷰 설정
        binding.rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(currentUser, chatList)
        Log.d("TAG : ", "이름은$currentUser ");
        binding.rvList.adapter = adapter

            // 입력 버튼
            binding.btnSend.setOnClickListener {
                var dateFormat = SimpleDateFormat("yy-MM-dd HH:mm")
                val datetime : LocalDateTime = LocalDateTime.now()
                val format = DateTimeFormatter.ofPattern("yy-MM-dd HH:ss")
                */
/*val currentMillis = System.currentTimeMillis()
                val currentDateTime =
                    Instant.ofEpochMilli(currentMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()*//*

                // 채팅창이 공백일 경우 버튼 비활성화
                if(binding.etChatting.text.toString() == ""){
                    Toast.makeText(context, "메세지를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else {
                    // 입력 데이터
                    val data = hashMapOf(
                        "nickname" to currentUser,
                        "contents" to binding.etChatting.text.toString(),
                        */
/*"time" to datetime.format(format),*//*

                        "time" to Timestamp.now(),
                        "room" to "2"
                        //채팅 번호
                    )
                    // Firestore에 기록
                    db.collection("Chat").add(data)
                        .addOnSuccessListener {
                            binding.etChatting.text.clear()
                            Log.w("ChatFragment", "Document added: $it")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "전송하는데 실패했습니다", Toast.LENGTH_SHORT).show()
                            Log.w("ChatFragment", "Error occurs: $e")

                        }
                }
            }

            return view
        }




override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enterTime = Date(System.currentTimeMillis())

        registration = db.collection("Chat")
                .orderBy("time", Query.Direction.DESCENDING)
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
                            val room = 1

                            val item = ChatData(nickname, contents, time, room)
                            chatList.add(item)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registration.remove()
        _binding = null
    }

}
*/
