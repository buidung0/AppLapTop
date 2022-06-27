package com.example.appbanhang.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.View.LoaiSpAdapter;
import com.example.appbanhang.View.SanphammoiAdapter;
import com.example.appbanhang.Model.LoaiSp;
import com.example.appbanhang.Model.SanPhamMoi;
import com.example.appbanhang.Retrofit.ApiBanHang;
import com.example.appbanhang.Retrofit.RetrofitClient;
import com.example.appbanhang.Util.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanphammoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        ActionBar(); // hàm gọi toolbar
        if(isConnect(this)){
            Toast.makeText(this, "kết nối internet thành công", Toast.LENGTH_SHORT).show();
            ActionViewFlipper(); //gọi chạy quảng cáo
            //get các file kết nối với php
            getLoaiSanPham();
            getSpMoi();
            getEventClick(); //hàm click vào các sự kiện trong toolbar như trang chủ, liên hệ, thông tin, ...
        }
        else {
            Toast.makeText(this, "không có internet, vui lòng kết nối", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), MacBookActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), LapTopActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent quanly = new Intent(getApplicationContext(), QuanLyActivity.class);
                        startActivity(quanly);
                        break;
                    case 4:
                        Intent thongtin = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(thongtin);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        Intent doimk = new Intent(getApplicationContext(), ChangePassActivity.class);
                        startActivity(doimk);
                        break;
                    case 7:
//                        Paper.book().delete("user");
                        Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangxuat);
                        break;

                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    mangSpMoi = sanPhamMoiModel.getResult();
                    spAdapter = new SanphammoiAdapter(getApplicationContext(), mangSpMoi);
                    recyclerViewManHinhChinh.setAdapter(spAdapter);
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Không kết nối được với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
        ));

    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaiSpModel -> {
                    if (loaiSpModel.isSuccess()){
                        mangloaisp = loaiSpModel.getResult();
                        //khởi tạo adapter
                        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                        listViewManHinhChinh.setAdapter(loaiSpAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdaPiDh8OX4wZDz1c6FW_Ypv7VmmeAgaPPhlxe-7IVtWiBwYI6bX5eIud4aHF_r3j-OhQ&usqp=CAU");  // cai nay la sao
        mangquangcao.add("https://phucanhcdn.com/media/news/7724_capture.PNG");
        mangquangcao.add("https://thietkehaithanh.com/wp-content/uploads/2019/01/thietkehaithanh-banner-laptop-800x300.png");
        mangquangcao.add("https://www.ankhang.vn/media/news/1211_Banner-hp-tang-tai-nghe.jpg");
        for (int i = 0; i<mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000); // thời gian chuyển đổi
        viewFlipper.setAutoStart(true); //tự động chạy
        // sét animation
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setAnimation(slide_in);
        viewFlipper.setAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START); //ĐƯA màn hình ra giữa
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        imgsearch = findViewById(R.id.imgsearch);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.memu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        //khởi tạo list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else{
                int totalItem = 0;
                for(int i=0 ;i< Utils.manggiohang.size(); i++){
                    totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
                }
                badge.setText(String.valueOf(totalItem));
            }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });


        }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i=0 ;i< Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    //hàm kiểm tra xem có kết nối internet hay không
    private boolean isConnect(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //Nho them quyen vao khong bi loi
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi !=null && wifi.isConnected()) ||(mobile !=null &&mobile.isConnected())){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}