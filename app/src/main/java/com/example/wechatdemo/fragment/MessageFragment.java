package com.example.wechatdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.ParcelUuid;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechatdemo.Activity.ChatActivity;
import com.example.wechatdemo.Activity.LaunchActivity;
import com.example.wechatdemo.Activity.LoginActivity;
import com.example.wechatdemo.MainActivity;
import com.example.wechatdemo.R;
import com.example.wechatdemo.adapter.MessageAdapter;
import com.example.wechatdemo.bean.Message;
import com.example.wechatdemo.util.RequestAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


public class MessageFragment extends Fragment {

    //用户账号number
    private String number;
    //List 消息列表
    private List<Message> messageList=new ArrayList<>();
    //listview
    private ListView listView;
    //Context
    private Context context=getActivity();

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doGetStulist();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
//        doGetStulist()
        listView=(ListView)view.findViewById(R.id.msg_list);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        number=((MainActivity)activity).getNumber();
        System.out.println("get number "+number);
    }


    

    public void doGetStulist(){
        SharedPreferences uinfo= getActivity().getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        System.out.println(number+"token");
        String token=uinfo.getString(number+" token","");
        System.out.println(token);
        String url="http://chatapi.dulx.cn/stulist";
        String param="?token="+token;
        url=url+param;
        System.out.println(url);
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
                        Message message=new Message();
                        message.setAvatar(object.getString("avatar"));
                        message.setNickname(object.getString("name"));
                        message.setDate("12:12");
                        message.setContent("你好");
                        message.setUid(object.getString("number"));
                        messageList.add(message);
                    }
                    listView.setAdapter(new MessageAdapter(getActivity(),messageList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                            MessageAdapter messageAdapter= (MessageAdapter) parent.getAdapter();
                            Message message= (Message) messageAdapter.getItem(position);
                            Bundle bundle=new Bundle();
                            bundle.putString("chat_f_name",message.getNickname());
                            bundle.putString("chat_f_number",message.getUid());
                            bundle.putString("user_number",number);
                            bundle.putString("chat_f_avatar",message.getAvatar());
                            Intent intent=new Intent(getActivity(),ChatActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
//                            getActivity().finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(String response) {
                System.out.println("Failed");
                return;
            }
        });
        requestAsyncTask.execute();
    }

}