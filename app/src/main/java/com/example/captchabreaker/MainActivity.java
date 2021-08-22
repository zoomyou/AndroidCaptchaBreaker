package com.example.captchabreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    public static boolean isOpen = false;
    public static boolean isWorking = false;
    public static String imageType = "0";
    final String WS_URL = "ws://121.5.113.98:80/socket/mini";

    private WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.captchaView);
        imageView.setImageResource(R.drawable.test);
    }

    public void openOrClose(View view){

        URI uri = URI.create(WS_URL);

        // 开启 webSocket 开始接收任务
        if (!isOpen){
            URI uri = URI.create(WS_URL);

            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "connection open", Toast.LENGTH_LONG).show();
                    Looper.loop();
                    MainActivity.isOpen = true;
                }

                @Override
                public void onMessage(String message) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "receive message:"+message, Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "connection close", Toast.LENGTH_LONG).show();
                    Looper.loop();
                    MainActivity.isOpen = false;
                }

                @Override
                public void onError(Exception ex) {
//                Toast.makeText(MainActivity.this, "connection error", Toast.LENGTH_LONG).show();
                    System.out.println("error:"+ex.getMessage());
                    MainActivity.isOpen = false;
                }
            };
            // TODO 开启任务接收

            try {
                webSocketClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // TODO 关闭任务接收
            webSocketClient.close();
            webSocketClient = null;
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