package com.example.wechatdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wechatdemo.R;
import com.example.wechatdemo.bean.Dialog;

import java.util.List;

public class DialogAdapter extends BaseAdapter {
    private List<Dialog> dialogList;
    private Context context;

    public DialogAdapter(Context context,List<Dialog> dialogList) {
        this.dialogList = dialogList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dialogList.size();
    }

    @Override
    public Object getItem(int position) {
        return dialogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Dialog dialog=dialogList.get(position);
        View view;
        ViewHolder holder;
        if(convertView==null){
            if (dialog.getType()==0){
                view = LayoutInflater.from(context).inflate(R.layout.chat_user_item,null);
                /* 缓存 */
                holder = new ViewHolder();
                holder.content=view.findViewById(R.id.usr_content);
                holder.avatar = view.findViewById(R.id.usr_avatar);
                view.setTag(holder);
            }else{
                view = LayoutInflater.from(context).inflate(R.layout.chat_friend_item,null);
                /* 缓存 */
                holder = new ViewHolder();
                holder.content=view.findViewById(R.id.fid_content);
                holder.avatar = view.findViewById(R.id.fid_avatar);
                view.setTag(holder);
            }
        }else {
            view =convertView;
            holder= (ViewHolder) view.getTag();
        }
        holder.content.setText(dialog.getContent());

        if (dialog.getType()==0){
            String base64 = dialog.getAvatar();
            if (base64.equals("")){
                holder.avatar.setImageResource(R.drawable.default_ava);
            }else{
                byte[] decodedString = Base64.decode(base64.split(",")[1], Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.avatar.setImageBitmap(decodedByte);
            }
        }else if (dialog.getType()==1){
            Glide.with(context)
                    .load(dialog.getAvatar())
                    .placeholder(R.drawable.default_ava)
                    .error(R.drawable.default_ava)
                    .into(holder.avatar);
        }

        return view;
    }

    private class ViewHolder {
        TextView content;
        ImageView avatar;
    }
}
