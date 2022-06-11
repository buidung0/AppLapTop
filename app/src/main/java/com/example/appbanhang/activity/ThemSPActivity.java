package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewbinding.ViewBinding;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.databinding.ActivityThemspBinding;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    int loai=0;
    TextView tensp,giasp,hinhanh,mota;
    Button btnthem;
    ActivityThemspBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewBinding a;
        binding= ActivityThemspBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(R.layout.activity_themsp);
        initView();
        initData();
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Loại 1");
        stringList.add("Loại 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "pdad", Toast.LENGTH_SHORT).show();
                themsanpham();
            }
        });
    }


    private void themsanpham() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.giasp.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || loai ==0 ){
            Toast.makeText(getApplicationContext(),"vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }else{
            compositeDisposable.add(apiBanHang.insertSp(str_ten,str_gia,str_hinhanh,str_mota,loai)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    messageModel -> {
                        if(messageModel.isSuccess()){
                            Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    },
                    throwable -> {
                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
            ));
        }
    }

    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
//        tensp = findViewById(R.id.tensp);
//        giasp = findViewById(R.id.giasp);
//        hinhanh = findViewById(R.id.hinhanh);
//        mota = findViewById(R.id.mota);
    }

}