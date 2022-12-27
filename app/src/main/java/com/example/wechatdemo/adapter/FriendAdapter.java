package com.example.wechatdemo.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wechatdemo.R;
import com.example.wechatdemo.bean.Friend;
import com.example.wechatdemo.bean.Message;

import java.util.List;

public class FriendAdapter extends BaseAdapter {
    private List<Friend> friendList;
    private Context context;

    public FriendAdapter(Context context,List<Friend> friendList){
        this.context=context;
        this.friendList=friendList;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Friend friend = friendList.get(position);
        View view;
        ViewHolder holder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.friend_list_item,null);
            holder=new ViewHolder();
            holder.nickname=(TextView) view.findViewById(R.id.friend_name);
            holder.avatar=(ImageView) view.findViewById(R.id.friend_avatar);
            view.setTag(holder);
        }else{
            view=convertView;
            holder= (ViewHolder) view.getTag();
        }
        holder.nickname.setText(friend.getNickname());
//        holder.avatar.setImageResource(friend.getAvatar());
        Glide.with(context)
                .load(friend.getAvatar())
                .placeholder(R.drawable.default_ava)
                .error(R.drawable.default_ava)
                .into(holder.avatar);
        return view;
    }

    private class ViewHolder {
        TextView nickname;
        ImageView avatar;
    }
}
