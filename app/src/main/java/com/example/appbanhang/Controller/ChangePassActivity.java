package com.example.appbanhang.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.Retrofit.ApiBanHang;
import com.example.appbanhang.Retrofit.RetrofitClient;
import com.example.appbanhang.Util.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePassActivity extends AppCompatActivity {
    TextView txtemail;
    EditText txtCurrentPassword,txtNewPassword,txtConfirmPassword;
    Button btnchagepass,btncancel;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Anhxa();
        Click();
    }

    private void Click() {
        txtemail.setText(Utils.user_current.getEmail());
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent huy = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(huy);
            }
        });
        btnchagepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String str_email = Utils.user_current.getEmail();
                String pass = Utils.user_current.getPassword();

                String str_currpass1 = txtCurrentPassword.getText().toString();//cat du lieu 2 dau
                String str_newpass = txtNewPassword.getText().toString();
                String str_renewpass = txtConfirmPassword.getText().toString();

                Log.d("AAAA",str_currpass1 + pass);

                if(TextUtils.isEmpty(str_currpass1))
                {
                    Toast.makeText(ChangePassActivity.this,"Chưa nhập Mật khẩu cũ",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_newpass))
                {
                    Toast.makeText(ChangePassActivity.this,"Chưa nhập Mật khẩu mới",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(str_renewpass))
                {
                    Toast.makeText(ChangePassActivity.this,"Cần xác nhận Mật Khẩu",Toast.LENGTH_LONG).show();
                }
                else if(str_newpass.equals(str_renewpass))//so sanh mat khau
                {

                    if (str_currpass1.equals(pass)) {

                        //lay data
                        compositeDisposable.add(apiBanHang.changepass(str_email, str_currpass1, str_renewpass)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        user_model -> {
                                            if (user_model.isSuccess()) {

                                                Utils.user_current.setPassword(str_renewpass);
                                                Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                                                startActivity(dangnhap);
                                                finish();
                                                Toast.makeText(getApplicationContext(), "Đổi MK thành công", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), user_model.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        },
                                        throwable -> {
                                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d("AAAA", "123 " + throwable.getMessage());
                                        }
                                ));

                    }
                    else {
                        Toast.makeText(ChangePassActivity.this, " Bạn nhập mật khẩu cũ sai", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(ChangePassActivity.this,"Mật Khẩu không khớp",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Anhxa() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtemail = findViewById(R.id.email);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtCurrentPassword = findViewById(R.id.txtCurrentPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        btncancel =findViewById(R.id.btncancel);
        btnchagepass = findViewById(R.id.btnchagepass);
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
