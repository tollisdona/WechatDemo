package com.example.wechatdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wechatdemo.R;
import com.example.wechatdemo.bean.Dialog;

import java.util.ArrayList;
import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
    private List<Dialog> dialogList;
    private Context context;
    private String user_number;

    public DialogAdapter (Context context,List<Dialog>dialogList,String user_number){
        this.context=context;
        this.dialogList=dialogList;
        this.user_number=user_number;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("dialsd"+dialogList);
        if (dialogList.size()>0){
        Dialog dialog = dialogList.get(position);
            System.out.println(dialog);
        if (dialog.getFrom().equals(user_number)) {
            holder.user_item.setVisibility(View.VISIBLE);
            holder.fid_item.setVisibility(View.GONE);
            holder.u_content.setText(dialog.getContent());
            String base64 = dialog.getAvatar();
            if (base64.equals("")) {
                holder.u_avatar.setImageResource(R.drawable.default_ava);
            } else {
                byte[] decodedString = Base64.decode(base64.split(",")[1], Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.u_avatar.setImageBitmap(decodedByte);
            }
        }else {
            holder.user_item.setVisibility(View.GONE);
            holder.fid_item.setVisibility(View.VISIBLE);
            holder.f_content.setText(dialog.getContent());
            Glide.with(context)
                    .load(dialog.getAvatar())
                    .placeholder(R.drawable.default_ava)
                    .error(R.drawable.default_ava)
                    .into(holder.f_avatar);
        }
    }
    }

    @Override
    public int getItemCount() {
        return dialogList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout user_item;
        LinearLayout fid_item;
        TextView u_content;
        TextView f_content;
        ImageView u_avatar;
        ImageView f_avatar;

        public ViewHolder(View view) {
            super(view);
            user_item = view.findViewById(R.id.user_chat);
            fid_item = view.findViewById(R.id.fid_chat);
            u_content=view.findViewById(R.id.usr_content);
            f_content=view.findViewById(R.id.fid_content);
            u_avatar=view.findViewById(R.id.usr_avatar);
            f_avatar=view.findViewById(R.id.fid_avatar);
        }
    }

}
