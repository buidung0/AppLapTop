package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.util.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    Button btndangnhap;
    TextView txtdangky;
    EditText edtemail,edtpass;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        Anhxa();
        initConTroll();
    }

    private void initConTroll() {
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dangnhap();
            }

            private void dangnhap() {
                String str_edtemail = edtemail.getText().toString().trim();//cat du lieu 2 dau
                String str_edtpass = edtpass.getText().toString().trim();
                if(TextUtils.isEmpty(str_edtemail)){
                    Toast.makeText(DangNhapActivity.this,"Bạn chưa nhập email",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_edtpass)){
                    Toast.makeText(DangNhapActivity.this,"Bạn chưa nhập Mật khẩu",Toast.LENGTH_LONG).show();
                }
                else {
                    //luu thong tin dang nhap
                    Paper.book().write("email",str_edtemail);
                    Paper.book().write("password",str_edtpass);
                    dangNhap(str_edtemail,str_edtpass);

                }

            }
        });
    }

    private void Anhxa() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangky = findViewById(R.id.txtdangky);
        btndangnhap = findViewById(R.id.btndangnhap);
        edtemail = findViewById(R.id.edtemail);
        edtpass = findViewById(R.id.edtpass);

        //read data
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null){
            edtemail.setText(Paper.book().read("email"));
            edtpass.setText(Paper.book().read("password"));
            if(Paper.book().read("isLogin")!= null){
                boolean flag = Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dangNhap(Paper.book().read("email"),Paper.book().read("password"));
                        }
                    },1000);
                }
            }

        }
    }

    private void dangNhap(String email,String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user_model -> {
                            if(user_model.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin",isLogin);
                                Utils.user_current = user_model.getResult().get(0);
                                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(home);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),user_model.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail() != null && Utils.user_current.getPassword() != null){
            edtemail.setText(Utils.user_current.getEmail());
            edtpass.setText(Utils.user_current.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
