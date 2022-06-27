package com.example.appbanhang.Retrofit;
import com.example.appbanhang.Model.DonHangModel;
import com.example.appbanhang.Model.LoaiSpModel;
import com.example.appbanhang.Model.MessageModel;
import com.example.appbanhang.Model.SanPhamMoiModel;
import com.example.appbanhang.Model.User_model;

import io.reactivex.rxjava3.core.Observable;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDOnHang(
            @Field("iduser") int id
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<User_model> reset(
            @Field("email") String email
    );
    @POST("changepass.php")
    @FormUrlEncoded
    Observable<User_model> changepass(
            @Field("email") String email,
            @Field("password") String password,
            @Field("newpassword") String newpassword
    );

    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
      @Field("search") String search
    );

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
            @Field("id") int id
    );

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int  id
    );

    @POST("update.php")
    @FormUrlEncoded
    Observable<MessageModel> updateSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int  idloai,
            @Field("id") int id
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(
            @Part MultipartBody.Part file
    );
}
