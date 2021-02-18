package com.auto.Autowallpaperchanger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.auto.Autowallpaperchanger.Adapters.ImageAdapterMutilSelect;
import com.auto.Autowallpaperchanger.Consts.Const;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


import org.json.JSONArray;

import java.util.ArrayList;

public class MultiSelectImagesActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static String TAG = "MultiSelectActivity";
    public static DisplayImageOptions options;
  //  private AdView adView;
    private LinearLayout banner_layout;
    private String folderName = "";
    /* access modifiers changed from: private */
   public ImageAdapterMutilSelect imageAdapter;
    private ArrayList<String> imageUrls;

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.multiselect);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            Toast.makeText(this, "null bar", 0).show();
        } else {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.folderName = bundle.getString(Const.UserFolderName);
            Log.e(TAG, this.folderName);
        } else {
            Log.e(TAG, "bundle null");
        }
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, (String) null, (String[]) null, "datetaken DESC");
        this.imageUrls = new ArrayList<>();
        for (int i = 0; i < imageCursor.getCount(); i++) {
            imageCursor.moveToPosition(i);
            this.imageUrls.add(imageCursor.getString(imageCursor.getColumnIndex("_data")));
            Log.e(TAG, "==Array Path==" + this.imageUrls.get(i));
        }
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_photo).showImageForEmptyUri(R.drawable.ic_menu_image_error).cacheInMemory().cacheOnDisc().build();
        this.imageAdapter = new ImageAdapterMutilSelect(this, this.imageUrls, (SparseBooleanArray) null, 3);
        GridView gridView = (GridView) findViewById(R.id.multi_select);
        gridView.setAdapter(this.imageAdapter);
        gridView.setColumnWidth(getResources().getDisplayMetrics().widthPixels / 4);
        this.banner_layout = (LinearLayout) findViewById(R.id.multiselect_banner);
//        this.adView = new AdView(this);
//        this.adView.setAdSize(AdSize.BANNER);
//        this.adView.setAdUnitId(Const.Ads_Banner);
//        this.adView.loadAd(new AdRequest.Builder().build());
//        this.banner_layout.addView(this.adView);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        ((CheckBox) menu.findItem(R.id.action_select_all).getActionView()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                MultiSelectImagesActivity.this.imageAdapter.selectAll(checked);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_photo, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done /*2131558573*/:
                menuDoneClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongConstant")
    private void menuDoneClick() {
        ArrayList<String> list = this.imageAdapter.getCheckedItem();
        if (list == null) {
            Toast.makeText(this, getResources().getString(R.string.toast_no_image_selected), 1).show();
            return;
        }
        setArrayImageSelected(list, this.folderName);
        sendBroadcast();
        MainActivity.createAlbumDialog.setMessage(getResources().getString(R.string.dialog_creating_text) + "\"" + this.folderName + "\"");
        MainActivity.createAlbumDialog.show();
        finish();
    }

    private void sendBroadcast() {
        sendBroadcast(new Intent("selected"));
    }

    private void setArrayImageSelected(ArrayList<String> field, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(Const.Pref, 0).edit();
        JSONArray jsArray = new JSONArray();
        if (field.size() > 0) {
            for (int i = 0; i < field.size(); i++) {
                jsArray.put(field.get(i));
                Log.e(TAG, field.get(i));
            }
            editor.putString(key, jsArray.toString());
        } else {
            editor.putString(key, (String) null);
        }
        editor.commit();
        Log.e(TAG, "save field " + key);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        imageLoader.stop();
    }
}
