package com.auto.Autowallpaperchanger.Adapters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public interface FasterLoadImage {
    void addBitmapToMemoryCache(String str, Bitmap bitmap);

    int calculateInSampleSize(BitmapFactory.Options options, int i, int i2);

    boolean cancelPotentialWork(int i, ImageView imageView);

    Bitmap decodeSampleedBitmapFromResource(Resources resources, int i);

    Bitmap getBipmapFromMemoryCache(String str);

    ImageAdapter.BitmapWorderTask getBitmapWorderTask(ImageView imageView);

    void loadBitmap(int i, ImageView imageView);
}
