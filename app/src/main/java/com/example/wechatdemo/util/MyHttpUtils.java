package com.example.wechatdemo.util;

import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyHttpUtils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public OkHttpClient client = new OkHttpClient();

    public String request(String url,JSONObject json,String method) throws IOException {
        if (method=="POST") {
            return this.post(url, json);
        }else if(method=="GET"){
            return this.get(url);
        }
        return null;
    }


    //post请求，返回String类型
    public String post(String url, JSONObject json) throws IOException {

        System.out.println("json" + json);
        RequestBody body = RequestBody.create(JSON, String.valueOf(json));
        System.out.println("request B :" + String.valueOf(json));
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")//添加头部
                .url(url)
                .post(body)
                .build();
        //发送请求获取响应
        try (Response response = client.newCall(request).execute()) {
            System.out.println("myhttputil+post+已经获取结果");
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String get(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        //发送请求获取响应
        try (Response response = client.newCall(request).execute()) {
            String TmpResult=response.body().string();
            return TmpResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
