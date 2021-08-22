package com.example.captchabreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    public static boolean isWorking = false;
//    public static String imageType = "0";
    final String WS_URL = "ws://www.captchabreakerog.ltd:80/socket/mini";
    // 图片显示区
    public ImageView imageView = null;
    // 文本框
    public EditText editText = null;
    // 开始键、提交键、放弃键
    public Button openOrClose = null;
    public Button submit = null;
    public Button abandon = null;

    private WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化全局的组件
        imageView = findViewById(R.id.captchaView);
        editText = findViewById(R.id.inputData);
        openOrClose = findViewById(R.id.openClose);
        submit = findViewById(R.id.submit);
        abandon = findViewById(R.id.abandon);
    }

    public void openOrClose(View view){
        // 未初始化时直接先初始化
        if (webSocketClient == null){
            System.out.println("init webSocket");
            initWebSocket();
        }

        if (!webSocketClient.isOpen()){
            // 如果是还未开启则开启
            if (webSocketClient.getReadyState().equals(WebSocket.READYSTATE.NOT_YET_CONNECTED)){
                System.out.println("webSocket not yet connected");
                webSocketClient.connect();
                imageView.setImageResource(R.drawable.waiting);
                openOrClose.setText(R.string.close);
            }

            // 如果是已关闭或正在关闭则将其开启
            if (webSocketClient.getReadyState().equals(WebSocket.READYSTATE.CLOSED) ||
                    webSocketClient.getReadyState().equals(WebSocket.READYSTATE.CLOSING)){
                System.out.println("webSocket already closed or is closing");
                webSocketClient.reconnect();
                imageView.setImageResource(R.drawable.waiting);
                openOrClose.setText(R.string.close);
            }
        } else {
            // 如果已开启则关闭
            if (webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
                System.out.println("webSocket already connected");
                webSocketClient.close();
                webSocketClient = null;
                imageView.setImageResource(R.drawable.disconnect);
                openOrClose.setText(R.string.open);
                showToast("connection closed");
            }
        }
    }

    public void submit(View view){

        if (!webSocketClient.isOpen()){
            Toast.makeText(MainActivity.this, "未开始接收任务", Toast.LENGTH_LONG).show();
            return;
        }

        webSocketClient.send("okkk");

        if (!isWorking){
            Toast.makeText(MainActivity.this, "当前无任务", Toast.LENGTH_LONG).show();
            return;
        }

    }

    public void abandon(View view){

        // TODO 放弃本次任务
        Toast.makeText(MainActivity.this, "放弃", Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message){
        if (Looper.myLooper() == null){
            Looper.prepare();
        }
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void initWebSocket(){
        // 初始化 webSocket 连接
        URI uri = URI.create(WS_URL);
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("onOpen()", "opened");
                System.out.println("listen webSocket open");
                showToast("connection open");
            }

            @Override
            public void onMessage(String message) {
                Log.i("onMessage()", "receive string message");
                System.out.println("listen webSocket receive message");
                JSONObject object = JSONObject.parseObject(message);
                String src_type = object.getString("src_type");
                String data = object.getString("show_data");
                imageView.setImageResource(R.drawable.test);
                if (src_type.equals("1")){
                    imageView.setImageBitmap(util.getBase64Bitmap(data));
                }
                if (src_type.equals("2")){
                    imageView.setImageBitmap(util.getUrlBitmap(data));
                }
                showToast("receive message");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("onClose()", "closed");
                System.out.println("listen webSocket close");
                imageView.setImageResource(R.drawable.disconnect);
                showToast("connection close");
            }

            @Override
            public void onError(Exception ex) {
                Log.i("onError", ex.getMessage());
                System.out.println("listen webSocket Error");
                imageView.setImageResource(R.drawable.disconnect);
                Log.e(ex.getMessage(), "Connection Error");
            }
        };
    }

}