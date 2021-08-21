package com.example.captchabreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private boolean isOpen = false;
    private boolean isWorking = false;
    private String imageType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.captchaView);
        imageView.setImageResource(R.drawable.test);
    }

    public void openOrClose(View view){
        // 开启 webSocket 开始接收任务
        if (!isOpen()){
            // TODO 开启任务接收
        } else {
            // TODO 关闭任务接收
        }
    }

    public void submit(View view){

        if (!isOpen){
            Toast.makeText(MainActivity.this, "未开始接收任务", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isWorking){
            Toast.makeText(MainActivity.this, "当前无任务", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO 提交任务结果
        setWorking();

    }

    public void abandon(View view){

        // TODO 放弃本次任务
        Toast.makeText(MainActivity.this, "放弃", Toast.LENGTH_SHORT).show();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen() {
        if (isOpen)
            isOpen = false;
        else
            isOpen = true;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking() {
        if (isWorking)
            isWorking = false;
        else
            isWorking = true;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}