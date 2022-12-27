package com.example.wechatdemo.Activity;



import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

//import org.apache.commons.codec.binary.Base64;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechatdemo.MainActivity;
import com.example.wechatdemo.R;
import com.example.wechatdemo.util.RequestAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;


import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button confirm;
    private Button back;
    private EditText number;
    private EditText name;
    private EditText lesson;
    private EditText pwd;
    private TextView up_ava;
    private ImageView avatar;
    private static final int RESULT_OPEN_IMAGE = 1;
    private static final int RESULT_CROP_IMAGE =2;
    private Bitmap bitmap ;//存放裁剪后的头像
    private String fileName;//头像名称
    private String picturePath;//头像路径
    private Uri pictureUri;//头像uri
    private final Context context=RegisterActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }

    public void init() {
        //初始化
        number = findViewById(R.id.rg_number);
        name = findViewById(R.id.rg_name);
        lesson = findViewById(R.id.rg_lesson);
        pwd = findViewById(R.id.rg_pwd);
        avatar=findViewById(R.id.rg_avatar);

        //按钮绑定
        confirm = findViewById(R.id.rg_confirm);
        confirm.setOnClickListener(this);
        back = findViewById(R.id.rg_back);
        back.setOnClickListener(this);
        up_ava=findViewById(R.id.up_ava);
        up_ava.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_ava:
                Intent ii = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ii, RESULT_OPEN_IMAGE);
                break;
            case R.id.rg_confirm:
                String snumber = String.valueOf(number.getText());
                String sname = String.valueOf(name.getText());
                String slesson = String.valueOf(lesson.getText());
                String spwd = String.valueOf(pwd.getText());
                if(TextUtils.isEmpty(snumber)||TextUtils.isEmpty(sname)||TextUtils.isEmpty(slesson)||TextUtils.isEmpty(spwd)){
                    Toast.makeText(RegisterActivity.this, "以上信息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("begin do register");
                try {
                    doRegister();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rg_back:
                Intent intent = new Intent(RegisterActivity.this, LaunchActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "resultCode" + resultCode);
        //从相册返回
        if(resultCode==RESULT_OK && data!=null){
            switch (requestCode){
                case RESULT_OPEN_IMAGE:
                    Uri selectedImage = data.getData();
                    Log.i("TAG","imageyrl "+selectedImage);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    fileName = getBitmapName(picturePath);
                    cursor.close();
                    //裁剪头像
                    cutImage(selectedImage);
                    break;
                case RESULT_CROP_IMAGE:
                    Bundle bundle=data.getExtras();
                    Bitmap bitmap=bundle.getParcelable("data");
                    avatar.setImageBitmap(bitmap);
                default:
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("tip", "The uri is not exist.");
        }
        Uri tempUri = uri;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale",true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
        startActivityForResult(intent, RESULT_CROP_IMAGE);
    }

    //获取图片的名称
    public String getBitmapName(String picPath) {
        String bitmapName = "";
        String[] s = picPath.split("/");
        bitmapName = s[s.length - 1];
        return bitmapName;
    }

    public void doRegister() throws JSONException {
        String snumber = String.valueOf(number.getText());
        String sname = String.valueOf(name.getText());
        String slesson = String.valueOf(lesson.getText());
        String spwd = String.valueOf(pwd.getText());
        //bitmap to base64
        System.out.println(snumber+" "+sname+""+slesson+spwd);
        System.out.println("1,tobase64");

        Bitmap bitmap2=((BitmapDrawable)avatar.getDrawable()).getBitmap();

        ///////***
        final Dialog dialog =new Dialog(context);
        ImageView imageView=new ImageView(context);
        imageView.setImageBitmap(bitmap2);
        dialog.setContentView(imageView);
        dialog.show();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG,100,out);
        byte[] bytes=out.toByteArray();
        System.out.println(bytes);
        String tbase64= Base64.encodeToString(bytes, Base64.NO_WRAP);
//        String base64= Base6
//        4.encodeBase64String(bytes);
        String base64 = "data:image/jpeg;base64,"+tbase64;
        System.out.println(base64);

        Map<String, String> map = new HashMap<>();
        map.put("number", snumber);
        map.put("name", sname);
        map.put("lesson", slesson);
        map.put("avatar",base64);
        map.put("key", "2022");
        map.put("password", spwd);
//        System.out.println("map:"+map);
        JSONObject jsonObject = new JSONObject(map);
        String url="http://chatapi.dulx.cn/reg";
        RequestAsyncTask requestAsyncTask=new RequestAsyncTask(RegisterActivity.this,url,jsonObject,"POST",false);
        requestAsyncTask.setOnDataFinishedListener(new RequestAsyncTask.onGetNetDataListener() {
            @Override
            public void onSucess(String response) {
                try {
                    JSONObject result=new JSONObject(response);
                    String msg=result.get("message").toString();
                    //保存base64在本地
                    SharedPreferences uinfo=getSharedPreferences("uinfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor=uinfo.edit();
                    editor.putString(snumber+" avatar",base64);
                    editor.putString(snumber+" name",sname);
                    editor.apply();
                    //提示信息
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //跳转页面
                    Intent intent = new Intent(RegisterActivity.this, LaunchActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    String msg=result.get("message").toString();
                    Toast.makeText(RegisterActivity.this, "注册失败:"+msg, Toast.LENGTH_SHORT).show();
                    Log.i("TAG","info:"+msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        requestAsyncTask.execute();
    }


}