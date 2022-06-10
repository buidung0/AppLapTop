package com.example.appbanhang.retrofit;
import com.example.appbanhang.model.LoaiSpModel;
import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.model.User_model;

import io.reactivex.rxjava3.core.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisanpham.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getloaisanphammoi.php")
    Observable<SanPhamMoiModel> getSpMoi();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangky.php")
    @FormUrlEncoded
    Observable<User_model> dangky(
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("role") String role
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<User_model> dangnhap(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<User_model> createOrder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
}
