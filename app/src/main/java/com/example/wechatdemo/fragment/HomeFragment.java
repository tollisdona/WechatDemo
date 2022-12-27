package com.example.wechatdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wechatdemo.MainActivity;
import com.example.wechatdemo.R;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment {

    //用户信息
    private String number;
    private String name;
    private String avatar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences uinfo=getActivity().getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        name=uinfo.getString(number+" name","");
        avatar=uinfo.getString(number+" avatar","");
        System.out.println(name+":"+avatar);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        number=((MainActivity)activity).getNumber();
        System.out.println("get number "+number);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        TextView name_view=view.findViewById(R.id.home_user_name);
        TextView number_view=view.findViewById(R.id.home_user_number);
        ImageView avatar_view=view.findViewById(R.id.home_user_avatar);
        name_view.setText(name);
        number_view.setText("学号:"+number);
        System.out.println(avatar);
//        avatar_view.setImageResource(R.drawable.default_ava);
        if (avatar.equals("")){
            avatar_view.setImageResource(R.drawable.ic_baseline_search_24);
        }else{
            System.out.println("avatar:::base64"+avatar);
            byte[] decodedString = Base64.decode(avatar.split(",")[1], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar_view.setImageBitmap(decodedByte);
        }
        return view;
    }
}