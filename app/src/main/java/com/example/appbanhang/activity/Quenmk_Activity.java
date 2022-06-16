package com.example.appbanhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.util.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Quenmk_Activity extends AppCompatActivity {
    Button btnlaymk;
    EditText eemail;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmk);
        Anhxa();
        click();
    }

    private void click() {
        btnlaymk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_eemail = eemail.getText().toString().trim() ;
                if(TextUtils.isEmpty(str_eemail)){
                    Toast.makeText(Quenmk_Activity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }
                else {
                    compositeDisposable.add(apiBanHang.reset(str_eemail)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    user_model -> {
                                        if(user_model.isSuccess()){
                                            Toast.makeText(getApplicationContext(),user_model.getMessage(),Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), ChangePassActivity.class);
                                            startActivity(intent);
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

                }}
        });
    }

    private void Anhxa() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        btnlaymk = findViewById(R.id.btnlaymk);
        eemail = findViewById( R.id.eemail);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
