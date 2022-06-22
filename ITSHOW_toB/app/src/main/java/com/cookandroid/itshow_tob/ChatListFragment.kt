package com.cookandroid.itshow_tob

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.stream.Collectors
import kotlin.collections.HashSet

class ChatListFragment : Fragment() {
    val db = Firebase.firestore
    var auth: FirebaseAuth? = null
    var name: String = " "
    var room_num = HashSet<String>()
    var r_user = HashSet<String>()
    var nicknames: String = ""
    lateinit var cAdapter: ChatListAdapter

    companion object {
        fun newInstance(): ChatActivity {
            return ChatActivity()
        }
    }

    private val fireDatabase = FirebaseAuth.getInstance()

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

// 그 방 번호에 있는 사람 수 얻기

        val view = inflater.inflate(R.layout.chat_list, container, false)
        val chatfragment_recyclerview = view.findViewById<RecyclerView>(R.id.chatfragment_recyclerview)
        chatfragment_recyclerview.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = RecyclerViewAdapter()

        var chatlistData = arrayListOf<ChatListData>()

// firebaseauth를 사용하기 위한 인스턴스 get
        auth = FirebaseAuth.getInstance()

        // 사용자가 들어가 있는 방 번호 얻기
        val user = auth!!.currentUser
        user?.let {
            name = user.displayName.toString()
            db.collection("Chat")
                    .whereEqualTo("nickname", name)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            var st = document.data.getValue("room")
                            room_num.add(st.toString())
                            // Log.d("mytag", st.toString())
                        }
                        // val set = room_num.toSet()
                        // var room_numr = set.toList()
                        for (rno in room_num) {
                            // 사용자 얻기
                            db.collection("Chat")
                                    .whereEqualTo("room", rno)
                                    .get()
                                    .addOnSuccessListener { result ->
                                        r_user.clear()
                                        for (document in result) {
                                            Log.d("tag : ", "---room : ${rno}")
                                            var nickname = document.data.getValue("nickname")
                                            // Log.d("mytag", nickname.toString())
                                            r_user.add(nickname.toString())
                                        }
                                        var usersString =
                                                r_user.stream().collect(Collectors.joining(","));

                                        Log.d("mytag", "------nickname${usersString}")
                                        db.collection("Chat")
                                                .whereEqualTo("room", rno)
                                                .orderBy("time", Query.Direction.DESCENDING).limit(1)
                                                .get()
                                                .addOnSuccessListener { result ->
                                                    var contents =
                                                            result.first().data.getValue("contents").toString()
                                                    chatlistData.add(
                                                            ChatListData(
                                                                    usersString,
                                                                    contents,
                                                                    rno
                                                            )
                                                    )

                                                    if(chatlistData!=null) {
                                                        cAdapter = ChatListAdapter(activity as Context, chatlistData)
                                                        chatfragment_recyclerview.adapter = cAdapter
                                                    }
                                                    for (item in chatlistData) {
                                                        Log.d("mytag", item.toString())
                                                    }
                                                }

                                                .addOnFailureListener { exception ->
                                                    Log.w(
                                                            ContentValues.TAG,
                                                            "----------Error getting documents.",
                                                            exception
                                                    )
                                                }

                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w(ContentValues.TAG, "----------Error getting documents.", exception)
                                    }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "----------Error getting documents.", exception)
                    }
            // 얻은 방번호 리스트를 이용해 for문 돌리기

        }
        return view
    }
}
