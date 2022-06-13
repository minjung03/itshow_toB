package com.cookandroid.itshow_tob

import android.app.Dialog
import android.content.Context
import android.view.Window


class LoadingDialog constructor(context: Context) : Dialog(context) {
    init {
        //다이얼로그 제목 안보이게
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading)
    }

}