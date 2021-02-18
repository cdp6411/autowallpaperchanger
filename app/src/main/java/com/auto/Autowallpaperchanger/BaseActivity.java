package com.auto.Autowallpaperchanger;

import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends AppCompatActivity {
    public static ImageLoader imageLoader = ImageLoader.getInstance();
}
