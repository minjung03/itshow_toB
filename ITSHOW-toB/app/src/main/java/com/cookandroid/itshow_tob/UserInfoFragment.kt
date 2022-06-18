package com.cookandroid.itshow_tob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.willy.ratingbar.BaseRatingBar

class UserInfoFragment : Fragment() {

    var auth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_info_fragment, container, false)

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

        return view
    }
}
