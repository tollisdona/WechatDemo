package com.example.wechatdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechatdemo.R;
import com.example.wechatdemo.bean.Message;

import java.util.List;


public class MessageAdapter extends BaseAdapter {
    private List<Message> messageList;
    private Context context;

    public MessageAdapter(Context context,List<Message>messageList){
        this.context=context;
        this.messageList=messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message message= messageList.get(position);
        View view;
        ViewHolder holder;
        if (convertView==null){
            view= LayoutInflater.from(context).inflate(R.layout.message_list_item,null);
            /* 缓存控件*/
            holder = new ViewHolder();
            holder.nickname=(TextView) view.findViewById(R.id.mlist_username);
            holder.date=(TextView) view.findViewById(R.id.mlist_datetime);
            holder.avatar=(ImageView) view.findViewById(R.id.mlist_avatar);
            holder.content=(TextView) view.findViewById(R.id.mlist_msg);
            view.setTag(holder);
        }else{
            view=convertView;
            holder=(ViewHolder) view.getTag();
        }
        holder.nickname.setText(message.getNickname());
        holder.content.setText(message.getContent());
        holder.date.setText(message.getDate());
        Glide.with(context)
                .load(message.getAvatar())
                .placeholder(R.drawable.default_ava)
                .error(R.drawable.default_ava)
                .into(holder.avatar);
        return view;
    }

    private class ViewHolder {
        TextView nickname;
        TextView content;
        TextView date;
        ImageView avatar;
        TextView uid;
    }
}
