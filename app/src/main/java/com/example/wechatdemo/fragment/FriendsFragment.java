package com.example.wechatdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wechatdemo.MainActivity;
import com.example.wechatdemo.R;
import com.example.wechatdemo.adapter.FriendAdapter;
import com.example.wechatdemo.adapter.MessageAdapter;
import com.example.wechatdemo.bean.Friend;
import com.example.wechatdemo.bean.Message;
import com.example.wechatdemo.util.RequestAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {

    //用户账号number
    private String number;
    //List 消息列表
    private List<Friend> friendList=new ArrayList<>();
    //listview
    private ListView listView;
    //Context
    private Context context=this.getActivity();
    // TODO: Rename parameter arguments, choose names that match


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doGetFriendList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_friends, container, false);
        listView=(ListView) view.findViewById(R.id.friend_list);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        number=((MainActivity)activity).getNumber();
        System.out.println("get number "+number);
    }

    public void doGetFriendList(){
        SharedPreferences uinfo= getActivity().getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        String token=uinfo.getString(number+" token","");
        String url="http://chatapi.dulx.cn/stulist";
        String param="?token="+token;
        url=url+param;
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask(this.getActivity(),url,null,"GET",true);
        requestAsyncTask.setOnDataFinishedListener(new RequestAsyncTask.onGetNetDataListener() {
            @Override
            public void onSucess(String response) {
                try {
                    System.out.println("success");
                    JSONObject result=new JSONObject(response);
                    JSONArray data=(JSONArray)result.get("data");
                    //存入数据messagelist
                    for (int i=0;i<data.length();i++){
                        JSONObject object=data.getJSONObject(i);
                        Friend friend=new Friend();
                        friend.setAvatar(object.getString("avatar"));
                        friend.setNickname(object.getString("name"));
                        friend.setUid(object.getString("number"));
                        friendList.add(friend);
                    }
                    listView.setAdapter(new FriendAdapter(getActivity(),friendList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String response) {
                System.out.println("Failed");
            }
        });
        requestAsyncTask.execute();
    }


}