package com.cookandroid.itshow_tob

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import com.cookandroid.itshow_tob.databinding.ReviewBinding

class Review : AppCompatActivity() {
    private var rBinding: ReviewBinding? = null
    private val binding get() = rBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review)

        rBinding = ReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reviewItemList = arrayListOf(
                ReviewItem(R.drawable.bin_heart, "하트하트"),
                ReviewItem(R.drawable.blue_person, "person")
        )

        binding.recycelerViewReview.adapter = ReviewAdapter(reviewItemList)


    }
}
