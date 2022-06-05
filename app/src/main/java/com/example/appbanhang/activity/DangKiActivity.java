package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.AipBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.util.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    AipBanHang aipBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Button btndangky;
    EditText email,pass,repass,name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        Anhxa();
        initConTroll();
    }

    private void Anhxa() {
        aipBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(AipBanHang.class);
        btndangky = findViewById(R.id.btndk);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
    }
    private void initConTroll() {
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangky();

            }

            private void dangky() {
                String str_email = email.getText().toString().trim();//cat du lieu 2 dau
                String str_pass = pass.getText().toString().trim();
                String str_name = name.getText().toString().trim();
                String str_phone = phone.getText().toString().trim();
                String str_repass = repass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email))
                {
                    Toast.makeText(DangKiActivity.this,"Chưa nhập email",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_name))
                {
                    Toast.makeText(DangKiActivity.this,"Chưa nhập Họ tên",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_phone))
                {
                    Toast.makeText(DangKiActivity.this,"Chưa nhập SĐT",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_pass))
                {
                    Toast.makeText(DangKiActivity.this,"Chưa nhập Mật Khẩu",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_repass))
                {
                    Toast.makeText(DangKiActivity.this,"Cần xác nhận Mật Khẩu",Toast.LENGTH_LONG).show();
                }
                else if(str_repass.equals(str_pass))//so sanh mat khau
                {
                    //lay data
                    compositeDisposable.add(aipBanHang.dangky(str_email,str_name,str_phone,str_pass,str_repass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    user_model -> {
                                        if(user_model.isSuccess()){
                                            Utils.user_current.setEmail(str_email);
                                            Utils.user_current.setPassword(str_pass);
                                            Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                                            startActivity(dangnhap);
                                            finish();
                                            Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),user_model.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                        Log.d("AAAA","123 "+throwable.getMessage());
                                    }
                            ));


                }
                else {
                    Toast.makeText(DangKiActivity.this,"Mật Khẩu không khớp",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}