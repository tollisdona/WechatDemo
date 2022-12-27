package com.example.wechatdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.wechatdemo.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_launch);

        //动画弹出效果
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final TranslateAnimation animation = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_SELF,0,TranslateAnimation.RELATIVE_TO_SELF,0,
                        TranslateAnimation.RELATIVE_TO_SELF,1,TranslateAnimation.RELATIVE_TO_SELF,0);
                animation.setDuration(400l);
                animation.setInterpolator(new DecelerateInterpolator());
                LinearLayout popup=(LinearLayout) findViewById(R.id.anim_btn);
                popup.setVisibility(View.VISIBLE);
                popup.startAnimation(animation);
            }
        },1000);

        //登录
        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
                startActivity(intent);
                LaunchActivity.this.finish();
            }
        });
        //注册
        Button reg = (Button) findViewById(R.id.btn_register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this,RegisterActivity.class);
                startActivity(intent);
                LaunchActivity.this.finish();
            }
        });
    }
    private void initWindow() {//初始化，将状态栏和标题栏设为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

}
