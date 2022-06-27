package com.example.appbanhang.View;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ItemClickListen;
import com.example.appbanhang.R;
import com.example.appbanhang.Controller.ChiTietActivity;
import com.example.appbanhang.Model.EventBus.SuaXoaEvent;
import com.example.appbanhang.Model.SanPhamMoi;
import com.example.appbanhang.Util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class SanphammoiAdapter extends RecyclerView.Adapter<SanphammoiAdapter.MyViewHolder> {

    Context context;
    List<SanPhamMoi>array;

    public SanphammoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanphammoi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtten.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ "Đ" );
        if(sanPhamMoi.getHinhanh().contains("http")){
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhanh);
        }else{
            String hinh = Utils.BASE_URL+"images/"+sanPhamMoi.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imghinhanh);
        }
        holder.setItemClickListener(new ItemClickListen() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context , ChiTietActivity.class);
                    intent.putExtra("chitiet",sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    EventBus.getDefault().postSticky(new SuaXoaEvent(sanPhamMoi));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtgia,txtten;
        ImageView imghinhanh;
        private ItemClickListen itemClickListener;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtgia = itemView.findViewById(R.id.itemsp_gia);
            txtten = itemView.findViewById(R.id.itemsp_ten);
            imghinhanh = itemView.findViewById(R.id.itemsp_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListen itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0,0,getAdapterPosition(),"Sửa");
            contextMenu.add(0,0,getAdapterPosition(),"Xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return false;
        }
    }
}
