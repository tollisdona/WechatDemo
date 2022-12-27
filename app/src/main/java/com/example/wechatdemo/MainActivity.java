package com.example.wechatdemo;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wechatdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Fragment[] fragments;
    //login 传递的number
    private String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置标题头
        TextView tip=findViewById(R.id.tx_tip);
        tip.setText("微信");

        System.out.println("main "+number);
        //初始化
        initFragments();

        //导航栏监听
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = item.getTitle().toString();
                switch (item.getItemId()){
                    case R.id.nav_wechat:
                        switchFragment(fragments[0]);
                        break;
                    case R.id.nav_dashboard:
                        switchFragment(fragments[1]);
                        break;
                    case R.id.nav_home:
                        switchFragment(fragments[2]);
                        break;
                }
                tip.setText(title);
                return true;
            }
        });
    }


    public void initFragments(){
        fragments=new Fragment[3];
        fragments[0]=getSupportFragmentManager().findFragmentById(R.id.fragment_message);
        fragments[1]=getSupportFragmentManager().findFragmentById(R.id.fragment_friends);
        fragments[2]=getSupportFragmentManager().findFragmentById(R.id.fragment_home);
        getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).show(fragments[0]).commit();
    }

    public void switchFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().hide(fragments[0]).hide(fragments[1]).hide(fragments[2]).show(fragment).commit();
    }

    //fragment on attach
    public String getNumber(){
        //获取bundle值
        Bundle bundle=this.getIntent().getExtras();
        number=bundle.getString("number");
        return number;
    }
}