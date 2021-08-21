package com.example.captchabreaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class util {
    public static Bitmap getUrlBitmap(String url){
        Bitmap bitmap = null;

        try {
            URL iconUrl = new URL(url);
            URLConnection connection = iconUrl.openConnection();
            HttpURLConnection URLConnection = null;

            if (url.charAt(4) == ':'){
                URLConnection = (HttpURLConnection) connection;
            }

            if (url.charAt(4) == 's'){
                URLConnection = (HttpsURLConnection) connection;
            }

            int length = URLConnection.getContentLength();

            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, length);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            bufferedInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }

        return bitmap;
    }

    public static Bitmap getBase64Bitmap(String base64){
        byte[] decodeString = Base64.decode(base64, Base64.DEFAULT);

        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);

        return decodeByte;
    }
}
