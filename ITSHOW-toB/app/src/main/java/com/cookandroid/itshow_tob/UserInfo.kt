package com.cookandroid.itshow_tob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class UserInfo : Fragment() {

    var auth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.user_info, container, false)

        val settings_icon = view.findViewById<ImageView>(R.id.image_settings)
        settings_icon.setOnClickListener {
            val intent = Intent(activity, Setting::class.java)
            startActivity(intent)
        }

        var user_name: TextView
        user_name = view.findViewById(R.id.user_info_name)

        var user_image: ImageView
        user_image = view.findViewById(R.id.user_info_Image)

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

            user_name.setText(displayName);
            //user_image.setImageBitmap(photoUrl)
            Glide.with(this).load(photoUrl).into(user_image);

            //Toast.makeText(this,  name.toString(), Toast.LENGTH_LONG).show()
            //Log.d(ContentValues.TAG, "handleSignInResult:personEmail $photoUrl")
        }

        return view
    }
}
