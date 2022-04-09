package com.cookandroid.itshow_tob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class Main extends AppCompatActivity {

    ImageView main_serch, main_daily_supplies, main_shopping, main_delivery, main_sundries, main_clothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        main_serch = findViewById(R.id.main_serch);
        main_daily_supplies = findViewById(R.id.main_daily_supplies);
        main_shopping = findViewById(R.id.main_shopping);
        main_delivery = findViewById(R.id.main_delivery);
        main_sundries = findViewById(R.id.main_sundries);
        main_clothing = findViewById(R.id.main_clothing);
    }
}
