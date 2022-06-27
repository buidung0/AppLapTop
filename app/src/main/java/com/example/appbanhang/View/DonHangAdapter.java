package com.example.appbanhang.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.Model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listDonHang;
    public DonHangAdapter( Context context,List<DonHang> listDonHang) {
        this.listDonHang = listDonHang;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listDonHang.get(position);
        holder.txtdonhang.setText("Đơn Hàng: "+ donHang.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adpter
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(donHang.getItem(), context);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(chiTietAdapter);

        holder.recyclerView.setRecycledViewPool(recycledViewPool);

    }

    @Override
    public int getItemCount() {
        return listDonHang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtdonhang;
        RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            recyclerView = itemView.findViewById(R.id.recycleviewchitiet);
        }
    }
}
