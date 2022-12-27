package com.example.wechatdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechatdemo.MainActivity;
import com.example.wechatdemo.R;
import com.example.wechatdemo.util.RequestAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText name=findViewById(R.id.tx_name);
        EditText pwd=findViewById(R.id.tx_pwd);

        //确认登录，用户名和密码随便输入但是不能为空
        Button confirm = (Button) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String snumber= String.valueOf(name.getText());
                String spwd= String.valueOf(pwd.getText());
                if(TextUtils.isEmpty(snumber)||TextUtils.isEmpty(spwd)){
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> map= new HashMap<>();
                map.put("number",snumber);
                map.put("password",spwd);
                JSONObject jsonObject =new JSONObject(map);
                String url="http://chatapi.dulx.cn/login";

                RequestAsyncTask requestAsyncTask=new RequestAsyncTask(LoginActivity.this,url,jsonObject,"POST",false);
                requestAsyncTask.setOnDataFinishedListener(new RequestAsyncTask.onGetNetDataListener() {
                    @Override
                    public void onSucess(String response) {
                        try {
                            JSONObject result=new JSONObject(response);
                            String msg=result.get("message").toString();
                            Object data=result.get("data");
                            //保存token
                            System.out.println(data.toString());
                            if (data.toString().equals("学号或密码错误")){
                                Toast.makeText(LoginActivity.this, "登录失败:学号或密码错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences uinfo=getSharedPreferences("uinfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor=uinfo.edit();
                            editor.putString(snumber+" uid:",snumber);
                            editor.putString(snumber+" token",data.toString());
                            editor.apply();
                            //提示信息
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            //跳转页面
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("number",snumber);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String response) {
                        JSONObject result= null;
                        try {
                            result = new JSONObject(response);
                            String msg=result.get("message").toString();
                            Toast.makeText(LoginActivity.this, "登录失败:"+msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                requestAsyncTask.execute();
            }
        });

        //返回
        Button back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,LaunchActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name",sname);
//                bundle.putString("password",spwd);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                LoginActivity.this.finish();
            }
        });


    }
}