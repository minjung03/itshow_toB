package com.cookandroid.itshow_tob

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Switch

import androidx.appcompat.app.AppCompatActivity

class Setting : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

         var img_SettingBack: ImageView
         var switch_appPush: Switch
         var layout_name_edit: RelativeLayout
         var layout_ID_PASS_edit: RelativeLayout
         var layout_Help: RelativeLayout
         var layout_UserDelete: RelativeLayout

        img_SettingBack = findViewById(R.id.img_SettingBack)
        switch_appPush = findViewById(R.id.switch_appPush)

        layout_name_edit = findViewById(R.id.layout_name_edit)
        layout_ID_PASS_edit = findViewById(R.id.layout_ID_PASS_edit)
        layout_Help = findViewById(R.id.layout_Help)
        layout_UserDelete = findViewById(R.id.layout_UserDelete)

        // 뒤로가기 버튼 (내 프로필 화면으로 이동)
        /*img_SettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user_info_intent = new Intent(getApplicationContext(), UserInfo.class);
                startActivity(user_info_intent);
            }
        });*/

        switch_appPush.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // 앱 알림 동의
            } else {
                // 앱 알림 비동의
            }
        }


        /* // 각 레이아웃 클릭 시 해당 화면으로 이동
        layout_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent name_edit_intent = new Intent(getApplicationContext(), NameEdit.class);
                startActivity(name_edit_intent);
            }
        });
        layout_ID_PASS_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Id_Pass_edit_intent = new Intent(getApplicationContext(), Id_Pass_Edit.class);
                startActivity(Id_Pass_edit_intent);
            }
        });
        layout_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Help_intent = new Intent(getApplicationContext(), Help.class);
                startActivity(Help_intent);
            }
        });
        layout_UserDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent user_Delete_intent = new Intent(getApplicationContext(), UserDelete.class);
                startActivity(user_Delete_intent);
            }
        });*/
    }
}