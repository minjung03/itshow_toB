package com.cookandroid.itshow_tob

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, "msg "+msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        Handler().postDelayed(Runnable() {
            run() {
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent);
            }
        }, 1500); // 1.5초 뒤 자동 화면 전환
    }
}
