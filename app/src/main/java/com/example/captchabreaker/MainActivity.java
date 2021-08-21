package com.example.captchabreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import io.crossbar.autobahn.websocket.WebSocketConnection;
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler;
import io.crossbar.autobahn.websocket.exceptions.WebSocketException;
import io.crossbar.autobahn.websocket.types.ConnectionResponse;


public class MainActivity extends AppCompatActivity {

    private boolean isOpen = false;
    private boolean isWorking = false;
    private String imageType = "0";
    final String WS_URL = "ws://www.captchabreakerog.ltd:8080/socket/mini";
    WebSocketConnection webSocketConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.captchaView);
        imageView.setImageResource(R.drawable.test);

        webSocketConnection = new WebSocketConnection();
    }

    public void openOrClose(View view){
        // 开启 webSocket 开始接收任务
        if (!isOpen()){
            // TODO 开启任务接收

            Toast.makeText(MainActivity.this, "正在连接...", Toast.LENGTH_LONG).show();
            try {
                webSocketConnection.connect(WS_URL, new WebSocketConnectionHandler(){

                    @Override
                    public void onClose(int code, String reason) {
                        System.out.println("onClose reason=" + reason);
                    }

                    @Override
                    public void onOpen() {
                        System.out.println("onOpen");
                        webSocketConnection.sendMessage("Hello!");
                    }

                    @Override
                    public void onConnect(ConnectionResponse response){
                        System.out.println("connnect to server");
                        Toast.makeText(MainActivity.this, "已连接", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMessage(String payload){
                        System.out.println("Receive message: " + payload);
                        webSocketConnection.sendMessage(payload);
                    }

                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        } else {
            // TODO 关闭任务接收
            webSocketConnection.sendClose();
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