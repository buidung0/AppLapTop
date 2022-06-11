package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.appbanhang.R;

public class QuanLyActivity extends AppCompatActivity {
    LinearLayout lay_themsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        initView();
        initControl();
    }

    private void initControl() {
        lay_themsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        lay_themsp = findViewById(R.id.lay_themsp);
    }
}