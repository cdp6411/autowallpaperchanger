package com.auto.Autowallpaperchanger.Receivers;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;

import com.auto.Autowallpaperchanger.Consts.Const;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class WallpaperReceiver extends BroadcastReceiver {
    private static String TAG = "onReceive";
    private static ArrayList<Integer> availableWallpaper;
    private static ArrayList<String> customWallpaper;
    private static ArrayList<String> downloadWallpaper;
    WallpaperManager wallpaperManager;

    private ArrayList<Integer> getWallpaperFromAvailable(String key, Context context) {
        String data = context.getSharedPreferences(Const.Pref, 0).getString(key, (String) null);
        ArrayList<Integer> list = new ArrayList<>();
        if (data != null) {
            try {
                JSONArray jsArray = new JSONArray(data);
                for (int i = 0; i < jsArray.length(); i++) {
                    list.add(Integer.valueOf(jsArray.optInt(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private ArrayList<String> getWallpaperFromDownload(String key, Context context) {
        String data = context.getSharedPreferences(Const.Pref, 0).getString(key, (String) null);
        ArrayList<String> list = new ArrayList<>();
        if (data != null) {
            try {
                JSONArray jsArray = new JSONArray(data);
                for (int i = 0; i < jsArray.length(); i++) {
                    list.add(jsArray.optString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private ArrayList<String> getWallpaperFromCustom(String key, Context context) {
        String data = context.getSharedPreferences(Const.Pref, 0).getString(key, (String) null);
        ArrayList<String> list = new ArrayList<>();
        if (data != null) {
            try {
                JSONArray jsArray = new JSONArray(data);
                for (int i = 0; i < jsArray.length(); i++) {
                    list.add(jsArray.optString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private String getValueFromPref(Context context, String key) {
        return context.getSharedPreferences(Const.Pref, 0).getString(key, "null");
    }

    private Bitmap decodeBitmapFile(String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        while (options.inSampleSize <= 16) {
            try {
                bitmap = BitmapFactory.decodeFile(path, options);
                Log.e(TAG, "decode successfull: " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "OutOfMemoryError: " + options.inSampleSize + "-" + path);
                options.inSampleSize++;
            }
        }
        return bitmap;
    }

    private Bitmap decodeBitmapResource(Context context, int resource) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        while (options.inSampleSize <= 16) {
            try {
                bitmap = BitmapFactory.decodeResource(context.getResources(), resource, options);
                Log.e(TAG, "decode successfull: " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "OutOfMemoryError: " + options.inSampleSize + "-" + resource);
                options.inSampleSize++;
            }
        }
        return bitmap;
    }

    private int rotationDegree(int rota) {
        if (rota == 6) {
            return 90;
        }
        if (rota == 3) {
            return 180;
        }
        if (rota == 8) {
            return 270;
        }
        return 0;
    }

    @SuppressLint("MissingPermission")
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        this.wallpaperManager = WallpaperManager.getInstance(context);
        SharedPreferences pref = context.getSharedPreferences(Const.Pref, 0);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        String valueFromPref = getValueFromPref(context, Const.Type);
        char c = 65535;
        switch (valueFromPref.hashCode()) {
            case 1270065833:
                if (valueFromPref.equals("Available")) {
                    c = 0;
                    break;
                }
                break;
            case 2029746065:
                if (valueFromPref.equals("Custom")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                availableWallpaper = getWallpaperFromAvailable(Const.WallpaperSet, context);
                int count = pref.getInt(Const.COUNT_AVAILABLE, 0);
                Log.e(TAG, "count: " + count);
                try {
                    Bitmap bitmap = decodeBitmapResource(context, availableWallpaper.get(count).intValue());
                    if (bitmap != null) {
                        this.wallpaperManager.setBitmap(bitmap);
                    }
                    int count2 = count + 1;
                    if (count2 >= availableWallpaper.size()) {
                        count2 = 0;
                    }
                    saveIntToPref(pref, Const.COUNT_AVAILABLE, count2);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            case 1:
                customWallpaper = getWallpaperFromCustom(Const.WallpaperSet, context);
                int count3 = pref.getInt(Const.COUNT_ALBUM, 0);
                Log.e(TAG, "count3: " + count3);
                try {
                    Bitmap bitmap2 = decodeBitmapFile(customWallpaper.get(count3));
                    int rotaDe = rotationDegree(new ExifInterface(customWallpaper.get(count3)).getAttributeInt("Orientation", 1));
                    Matrix matrix = new Matrix();
                    matrix.postRotate((float) rotaDe);
                    Bitmap bitmap3 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                    if (pref.getBoolean(Const.Auto_Crop, false)) {
                        bitmap3 = Bitmap.createScaledBitmap(bitmap3, width, height, true);
                    }
                    if (bitmap3 != null) {
                        this.wallpaperManager.setBitmap(bitmap3);
                    }
                    int count32 = count3 + 1;
                    if (count32 >= customWallpaper.size()) {
                        count32 = 0;
                    }
                    saveIntToPref(pref, Const.COUNT_ALBUM, count32);
                    return;
                } catch (IOException e2) {
                    Log.e(TAG, "error: ");
                    e2.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    private void saveIntToPref(SharedPreferences pref, String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
