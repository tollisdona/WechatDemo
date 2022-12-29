package com.example.wechatdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechatdemo.R;
import com.example.wechatdemo.adapter.DialogAdapter;
import com.example.wechatdemo.bean.Dialog;
import com.example.wechatdemo.util.MyHttpUtils;
import com.example.wechatdemo.util.RequestAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    //bundle 对方姓名、学号
    private String f_name;
    private String f_number;
    private String f_avatar;
    //用户shared preference 学号、token
    private String user_token;
    private String user_number;
    private String user_avatar;
    //访问地址
    private String url;
    //对话消息list
    static private RecyclerView recyclerView;
    private int listLength;
    static List<Dialog> dialogList = new ArrayList<>();
    private DialogAdapter dialogAdapter;
    //实例化线程
    private HandlerThread mHandlerThread;
    private Handler queryHandler;
    private int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //获取bundle的传值
        Bundle bundle = this.getIntent().getExtras();
        f_name = bundle.getString("chat_f_name");
        f_number = bundle.getString("chat_f_number");
        f_avatar = bundle.getString("chat_f_avatar");// 格式为url
        user_number = bundle.getString("user_number");
        //设置标题栏
        TextView chat_f_name = findViewById(R.id.chat_f_name);
        chat_f_name.setText(f_name);

        //获取缓存中的token，拼接url
        SharedPreferences uinfo = getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        user_token = uinfo.getString(user_number + " token", "");
        user_avatar = uinfo.getString(user_number + " avatar", ""); //格式base64


        //初始化控件
        ImageView voice = findViewById(R.id.voice);
        EditText input = findViewById(R.id.input);
        ImageView stickers = findViewById(R.id.stickers);
        recyclerView = findViewById(R.id.chat_msg);
        dialogAdapter = new DialogAdapter(ChatActivity.this, dialogList, user_number);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerView.setAdapter(dialogAdapter);

//        dialogList.clear();

        url = "http://chatapi.dulx.cn/getmsg";
        String param = "?token=" + user_token;
        url += param + "&from=" + user_number + "&to=" + f_number;

        mHandlerThread = new HandlerThread("queryMsg");
        mHandlerThread.start();
        Handler muiHandler = new Handler();
        //查询线程
        queryHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    try {
                        System.out.println("执行次数：" + flag);
                        MyHttpUtils myHttpUtils = new MyHttpUtils();
                        String result = myHttpUtils.request(url, null, "GET");
                        doHandleDialog(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    muiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("1111 set listview" + dialogList);
                            dialogAdapter.notifyItemRangeChanged(listLength, dialogList.size() - listLength);
                            recyclerView.scrollToPosition(dialogList.size() - 1);
                            flag++;
                            listLength = dialogList.size();
                            queryHandler.sendEmptyMessageDelayed(1, 5000);
                        }
                    });
                }
            }
        };

        Message message = new Message();
        message.what = 1;
        message.obj = null;
        queryHandler.sendEmptyMessage(1);

        //发送按钮监听：
        Button send = findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(input.getText());
                if (text == "") {
                    Toast.makeText(ChatActivity.this, "不可发送空消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                input.setText("");
                doSendMsg(text);

            }
        });

        //返回按钮监听
        Button mGoBack = findViewById(R.id.chat_back);
        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.finish();
            }
        });
    }

    public void doQueryMsg() {
        //拼接url
        url = "http://chatapi.dulx.cn/getmsg";
        String param = "?token=" + user_token;
        url += param + "&from=" + user_number + "&to=" + f_number;
        System.out.println(url);

        System.out.println("from to url" + url);
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask(ChatActivity.this, url, null, "GET", false);
        requestAsyncTask.setOnDataFinishedListener(new RequestAsyncTask.onGetNetDataListener() {
            @Override
            public void onSucess(String response) {
                System.out.println("success");
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.get("data") == null) {
                        System.out.println("未查询到消息");
                        return;
                    }
                    JSONArray data = (JSONArray) result.get("data");
                    //存入数据Dialog
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject1 = data.getJSONObject(i);
                        Dialog dialog = new Dialog();
                        String from = String.valueOf(jsonObject1.get("from"));
                        if (from.equals(f_number)) {
                            dialog.setAvatar(f_avatar);
                            dialog.setContent((String) jsonObject1.get("content"));
                            dialog.setFrom(f_number);
                        } else if (from.equals(user_number)) {
                            dialog.setAvatar(user_avatar);
                            dialog.setContent((String) jsonObject1.get("content"));
                            dialog.setFrom(user_number);
                        } else {
                            System.out.println("ERROOR");
                            return;
                        }
                        dialogList.add(dialog);
                    }
//                    listView.setSelection(listView.getCount()-1);
//                    listView.setAdapter(new DialogAdapter(ChatActivity.this, dialogList,user_number));
                    recyclerView.smoothScrollToPosition(dialogList.size() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    System.out.println("没有发送过消息");
                }
            }

            @Override
            public void onFailure(String response) {
                System.out.println("查询消息：失败");
                JSONObject result = null;
                try {
                    result = new JSONObject(response);
                    int code = (int) result.get("code");
                    String msg = (String) result.get("msg");
                    System.out.println("Failed,code:" + code + "msg:" + msg);
                    Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestAsyncTask.execute();
    }

    public void doSendMsg(String content) {
        //拼接url
        url = "http://chatapi.dulx.cn/sendone";
        String param = "?token=" + user_token;
        url = url + param;
        System.out.println(url);

        Map<String, Object> map = new HashMap<>();
        map.put("from", user_number);
        map.put("to", f_number);
        map.put("msg_type", 1);
        map.put("content", content);

        Dialog dialog0 = new Dialog();
        dialog0.setAvatar(user_avatar);
        dialog0.setFrom(user_number);
        dialog0.setContent(content);

        JSONObject jsonObject = new JSONObject(map);
        System.out.println(jsonObject);
        RequestAsyncTask requestAsyncTask = new RequestAsyncTask(ChatActivity.this, url, jsonObject, "POST", false);
        requestAsyncTask.setOnDataFinishedListener(new RequestAsyncTask.onGetNetDataListener() {
            @Override
            public void onSucess(String response) {
                System.out.println("success");
                dialogList.add(dialog0);
                listLength++;
                dialogAdapter.notifyItemInserted(dialogAdapter.getItemCount() - 1);
                recyclerView.scrollToPosition(dialogList.size() - 1);
            }

            @Override
            public void onFailure(String response) {
                try {
                    JSONObject result = null;
                    result = new JSONObject(response);
                    int code = (int) result.get("code");
                    String msg = (String) result.get("msg");
                    System.out.println("Failed,code:" + code + "msg:" + msg);
                    Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        requestAsyncTask.execute();
    }


    @Override
    protected void onDestroy() {
        mHandlerThread.quit();
        dialogList.clear();
        listLength = 0;
        flag = 0;
        super.onDestroy();
    }


    public void doHandleDialog(String response) {
        try {
            System.out.println("处理resp");
            JSONObject result = new JSONObject(response);
            JSONArray data = (JSONArray) result.get("data");
            System.out.println("dataL:" + data);
            //存入数据Dialog
            int dlength = data.length();
            System.out.println(dlength + ":dlength" + "  listlieng:" + listLength);
            if (dlength > listLength) {
                for (int i = listLength; i < dlength; i++) {
                    JSONObject jsonObject1 = data.getJSONObject(i);
                    Dialog dialog = new Dialog();
                    String from = String.valueOf(jsonObject1.get("from"));
                    System.out.println("chakna:" + jsonObject1);
                    if (from.equals(f_number)) {
                        dialog.setAvatar(f_avatar);
                        dialog.setContent((String) jsonObject1.get("content"));
                        dialog.setFrom(f_number);
                    } else if (from.equals(user_number)) {
                        dialog.setAvatar(user_avatar);
                        dialog.setContent((String) jsonObject1.get("content"));
                        dialog.setFrom(user_number);
                    } else {
                        System.out.println("ERROOR");
                        return;
                    }
                    dialogList.add(dialog);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.out.println("没有发送过消息");
        }
    }
}