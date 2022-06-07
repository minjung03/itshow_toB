package com.cookandroid.itshow_tob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class UserInfo : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_info, container, false)

        val settings_icon = view.findViewById<ImageView>(R.id.image_settings)
        settings_icon.setOnClickListener{
            val intent = Intent(activity, Setting::class.java)
            startActivity(intent)
        }

        return view
    }
}
