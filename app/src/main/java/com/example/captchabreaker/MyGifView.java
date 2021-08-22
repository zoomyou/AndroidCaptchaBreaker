package com.example.captchabreaker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyGifView extends View {
    private long movieStart;
    private Movie movie;

    @SuppressLint("ResourceType")
    public MyGifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        movie = Movie.decodeStream(getResources().openRawResource(R.drawable.waiting));
    }
}
