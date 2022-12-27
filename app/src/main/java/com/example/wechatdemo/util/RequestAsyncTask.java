package com.example.wechatdemo.util;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.JetPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.wechatdemo.Activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RequestAsyncTask extends AsyncTask<String,Void,String> {
    private Context context;
    private String url;
    private JSONObject json;
    private String method;
    private ProgressDialog dialog;
    private boolean flag=false;
    onGetNetDataListener listener;

    private void initDialog(){
        dialog=new ProgressDialog(context);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在加载中....");
    }

    //回调接口，处理成功、失败的情况
    public interface onGetNetDataListener {
        public void onSucess(String response);
        public void onFailure(String response);
    }

    public void setOnDataFinishedListener(
            onGetNetDataListener onDataFinishedListener) {
        this.listener = onDataFinishedListener;
    }

    //构造函数
    public RequestAsyncTask(Context context, String url, JSONObject json, String method,boolean flag) {
        this.context = context;
        this.url = url;
        this.json = json;
        this.method=method;
        this.flag=flag;
        initDialog();
    }

    //运行主线程中,通常用来进行控件的初始化
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (flag){
            dialog.show();
        }
    }

    //运行在子线程中,进行耗时操作等逻辑
    @Override
    protected String doInBackground(String... strings) {
        MyHttpUtils myHttpUtils = new MyHttpUtils();
        try {
            String result = myHttpUtils.request(url,json,method);
            System.out.println("1");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if(s==null){
                Log.i("TAG","INFO: 请求超时，无返回结果");
                return;
            }
            if (flag){
                dialog.dismiss();
            }
            System.out.println("PsotExecute 返回的结果："+s);
            JSONObject jsonobject = new JSONObject(s);
            int code= (int) jsonobject.get("code");
            if(code == 0){
                listener.onSucess(s);
            }else {
                listener.onFailure(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
