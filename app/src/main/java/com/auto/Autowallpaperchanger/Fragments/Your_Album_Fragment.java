package com.auto.Autowallpaperchanger.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.auto.Autowallpaperchanger.Adapters.ImageAdapterMutilSelect;
import com.auto.Autowallpaperchanger.Consts.Const;
import com.auto.Autowallpaperchanger.MainActivity;
import com.auto.Autowallpaperchanger.MultiSelectImagesActivity;
import com.auto.Autowallpaperchanger.R;
import com.auto.Autowallpaperchanger.Receivers.WallpaperReceiver;
import com.auto.Autowallpaperchanger.models.SpinnerItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Your_Album_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int READ_EXTERNAL_STORAGE = 1;
    public static String TAG = "CustomActivity";
    public static Button btSetWallpaper;
    public static Spinner cSpinnerTime;

    public static LinearLayout spinner_your_album;
    /* access modifiers changed from: private */
    public static ArrayList<String> customItemSellected;
    public static String folderName;
    public static ImageAdapterMutilSelect imageCustomAdapter;
    /* access modifiers changed from: private */
    public static int posItemSelected;
    /* access modifiers changed from: private */
    public static SharedPreferences pref;
    /* access modifiers changed from: private */
    public static int timeInterval = 60000;
    private Animation anim_click_here;
    private GridView cGridView;
    private CheckBox checkAll;
    private ImageView click_here;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private Animation fab_close;
    private Animation fab_open;
    /* access modifiers changed from: private */
    public int height;
    private boolean isFabOpen = false;
    /* access modifiers changed from: private */
    public Resources mResources;
    /* access modifiers changed from: private */
    public AlarmManager manager;
    /* access modifiers changed from: private */
    public PendingIntent pendingIntent;
    private View rootView;
    private Animation rotate_backward;
    private Animation rotate_forward;
    /* access modifiers changed from: private */
    public ProgressDialog setWallDialog;
    private TextView textViewNoAlbum;
    private Animation text_fab_close;
    private Animation text_fab_open;
    private TextView txtFab1;
    private TextView txtFab2;
    private TextView txtFab3;
    /* access modifiers changed from: private */
    public WallpaperManager wallpaperManager;
    /* access modifiers changed from: private */
    public int width;
    private AdView mAdView;

    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd1;

    private enum Time {
        ONEMIN,
        THREEMIN,
        FIVEMIN,
        TENMIN,
        THIRTYMIN,
        ONEHOUR,
        TWELVEHOUR,
        TWENTYFOURHOUR
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.your_album_layout, container, false);
        mAdView =this.rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd1 = new InterstitialAd(getActivity());
        mInterstitialAd1.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd1.loadAd(new AdRequest.Builder().build());


        btSetWallpaper = (Button) this.rootView.findViewById(R.id.c_bt_set);
        btSetWallpaper.setOnClickListener(this);
        btSetWallpaper.setEnabled(false);
        spinner_your_album = (LinearLayout)rootView.findViewById(R.id.spinner_your_album);
        cSpinnerTime = (Spinner) this.rootView.findViewById(R.id.c_spinner_time);
        cSpinnerTime.setEnabled(false);
        cSpinnerTime.setOnItemSelectedListener(this);
        this.cGridView = (GridView) this.rootView.findViewById(R.id.c_gridview);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
        this.cGridView.setColumnWidth(this.width / 4);
        this.manager = (AlarmManager) getActivity().getSystemService("alarm");
        this.wallpaperManager = WallpaperManager.getInstance(getActivity());
        pref = getActivity().getSharedPreferences(Const.Pref, 0);
        this.mResources = getResources();
        this.setWallDialog = new ProgressDialog(getActivity());
        this.setWallDialog.setMessage(this.mResources.getString(R.string.dialog_setting_sms));
        this.setWallDialog.setCancelable(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            posItemSelected = bundle.getInt(Const.ALBUM_FIELD_SELECTED, 0);
        }
        this.textViewNoAlbum = (TextView) this.rootView.findViewById(R.id.text_no_album);
        this.click_here = (ImageView) this.rootView.findViewById(R.id.click_here);
        this.anim_click_here = AnimationUtils.loadAnimation(getActivity(), R.anim.click_here);
        setAlbumGaller();
        setHasOptionsMenu(true);
        this.fab = (FloatingActionButton) this.rootView.findViewById(R.id.fab);
        this.fab1 = (FloatingActionButton) this.rootView.findViewById(R.id.fab1);
        this.fab2 = (FloatingActionButton) this.rootView.findViewById(R.id.fab2);
        this.fab3 = (FloatingActionButton) this.rootView.findViewById(R.id.fab3);
        this.fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        this.fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        this.rotate_forward = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_forward);
        this.rotate_backward = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_rotate_backward);
        this.text_fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.text_fab_open);
        this.text_fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.text_fab_close);
        this.fab.setOnClickListener(this);
        this.fab1.setOnClickListener(this);
        this.fab2.setOnClickListener(this);
        this.fab3.setOnClickListener(this);
        this.txtFab1 = (TextView) this.rootView.findViewById(R.id.txt_fab1);
        this.txtFab2 = (TextView) this.rootView.findViewById(R.id.txt_fab2);
        this.txtFab3 = (TextView) this.rootView.findViewById(R.id.txt_fab3);
        return this.rootView;
    }

    public void onPrepareOptionsMenu(Menu menu) {
        this.checkAll = (CheckBox) menu.getItem(0).getActionView();
        this.checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (Your_Album_Fragment.imageCustomAdapter != null) {
                    Your_Album_Fragment.imageCustomAdapter.selectAll(checked);
                }
            }
        });
        if (MainActivity.listAlbum == null || MainActivity.listAlbum.size() == 0) {
            this.checkAll.setEnabled(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity, menu);
    }

    @SuppressLint("WrongConstant")
    private void setAlbumGaller() {
        if (MainActivity.listAlbum == null || MainActivity.listAlbum.size() == 0) {
            this.click_here.startAnimation(this.anim_click_here);
            return;
        }
        this.click_here.clearAnimation();
        this.click_here.setVisibility(4);
        this.textViewNoAlbum.setVisibility(8);
        customItemSellected = getArray(MainActivity.listAlbum.get(posItemSelected).getTitle());
        customItemSellected = updateList();
        if (("C" + customItemSellected).equals(pref.getString(Const.KeyField, ""))) {
            imageCustomAdapter = new ImageAdapterMutilSelect(getActivity(), customItemSellected, getBooleanArraySaved(Const.keyArrayBooleanItemSelected), 1);
            cSpinnerTime.setSelection(pref.getInt(Const.TimeToChange, 0));
        } else {
            imageCustomAdapter = new ImageAdapterMutilSelect(getActivity(), customItemSellected, (SparseBooleanArray) null, 1);
            cSpinnerTime.setSelection(0);
        }
        this.cGridView.setAdapter(imageCustomAdapter);
        int count = imageCustomAdapter.getCounSelected();
        if (count > 0) {
            btSetWallpaper.setEnabled(true);
        } else {
            btSetWallpaper.setEnabled(false);
        }
        if (count > 1) {
            cSpinnerTime.setEnabled(true);
        } else {
            cSpinnerTime.setEnabled(false);
        }
    }

    private ArrayList<String> updateList() {
        ArrayList<String> temp = new ArrayList<>();
        int size = customItemSellected.size();
        for (int i = 0; i < size; i++) {
            if (new File(customItemSellected.get(i)).exists()) {
                temp.add(customItemSellected.get(i));
            }
        }
        return temp;
    }

    @SuppressLint("WrongConstant")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab3:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                showDialogAlbum();
                animateFAB();
                return;
            case R.id.fab2:
                sortByName();
                animateFAB();
                return;
            case R.id.fab1:
                if (mInterstitialAd1.isLoaded()) {
                    mInterstitialAd1.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                showDialogDelete();
                animateFAB();
                return;
            case R.id.fab:
                this.click_here.clearAnimation();
                this.click_here.setVisibility(4);
                animateFAB();
                return;
            case R.id.c_bt_set:
                Toast.makeText(getActivity(), "Wallpaper is set after 1 minute", Toast.LENGTH_SHORT).show();
                try {
                    setWallpaper();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.a_spinner_time) {
            setTime(pos);
            Log.d(TAG, "Select " + pos);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 1) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (grantResults.length == 1 && grantResults[0] == 0) {
            goToGallary(folderName);
        } else {
            showToast(getString(R.string.request_permission_error));
        }
    }

    private void setTime(int pos) {
        switch (Time.values()[pos]) {
            case ONEMIN:
                timeInterval = 60000;
                return;
            case THREEMIN:
                timeInterval = 180000;
                return;
            case FIVEMIN:
                timeInterval = 300000;
                return;
            case TENMIN:
                timeInterval = 600000;
                return;
            case THIRTYMIN:
                timeInterval = 1800000;
                return;
            case ONEHOUR:
                timeInterval = 3600000;
                return;
            case TWELVEHOUR:
                timeInterval = 43200000;
                return;
            case TWENTYFOURHOUR:
                timeInterval = 86400000;
                return;
            default:
                return;
        }
    }



    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void delelteAlbum(int pos) {
        saveToPref(MainActivity.listAlbum.get(pos).getTitle(), (String) null);
        MainActivity.listAlbum.remove(pos);
        MainActivity.albumAdapter.notifyDataSetChanged();
        saveField(MainActivity.listAlbum, Const.Custom_Field_Added);
        MainActivity.spinner_field.setAdapter(MainActivity.albumAdapter);
        if (MainActivity.listAlbum.size() == 0) {
            imageCustomAdapter.selectAll(false);
            this.cGridView.setAdapter((ListAdapter) null);
            this.textViewNoAlbum.setVisibility(0);
            this.checkAll.setEnabled(false);
        }
    }

    private void sortByName() {
        if (MainActivity.listAlbum.size() == 0) {
            showToast(getString(R.string.text_no_album));
            return;
        }
        Collections.sort(MainActivity.listAlbum, new Comparator<SpinnerItem>() {
            public int compare(SpinnerItem item1, SpinnerItem item2) {
                return item1.getTitle().compareTo(item2.getTitle());
            }
        });
        MainActivity.albumAdapter.notifyDataSetChanged();
        MainActivity.spinner_field.setAdapter(MainActivity.albumAdapter);
        saveField(MainActivity.listAlbum, Const.Custom_Field_Added);
    }

    private void showDialogAlbum() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_newfolder, (ViewGroup) null);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_newfolder);
        View viewTitle = inflater.inflate(R.layout.custom_dialog, (ViewGroup) null);
        ((TextView) viewTitle.findViewById(R.id.title)).setText(this.mResources.getString(R.string.dialog_create_new_album_title));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(view);
        dialog.setCustomTitle(viewTitle);
        dialog.setMessage(this.mResources.getString(R.string.dialog_create_new_album_sms));
        dialog.setPositiveButton(this.mResources.getString(R.string.dialog_button_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String name = editText.getText().toString();
                if (Your_Album_Fragment.this.nameNotExist(name)) {
                    Your_Album_Fragment.this.goToGellaryMultiSelect(name);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setNegativeButton(this.mResources.getString(R.string.dialog_button_cancel), (DialogInterface.OnClickListener) null);
        dialog.show();
    }

    private void showDialogDelete() {
        if (MainActivity.listAlbum.size() == 0) {
            showToast(getString(R.string.text_no_album));
            return;
        }
        View viewTitle = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, (ViewGroup) null);
        ((TextView) viewTitle.findViewById(R.id.title)).setText(this.mResources.getString(R.string.dialog_delete_album_title));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCustomTitle(viewTitle);
        dialog.setMessage(this.mResources.getString(R.string.dialog_delete_new_album_sms));
        dialog.setPositiveButton(this.mResources.getString(R.string.dialog_button_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Your_Album_Fragment.this.delelteAlbum(Your_Album_Fragment.posItemSelected);
            }
        });
        dialog.setNegativeButton(this.mResources.getString(R.string.dialog_button_no), (DialogInterface.OnClickListener) null);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showDialogRequestPermission() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(this.mResources.getString(R.string.dialog_request_permission));
        dialog.setPositiveButton(this.mResources.getString(R.string.dialog_button_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                FragmentCompat.requestPermissions(Your_Album_Fragment.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
            }
        });
        dialog.setNegativeButton(this.mResources.getString(R.string.dialog_button_no), (DialogInterface.OnClickListener) null);
        dialog.setCancelable(false);
        dialog.show();
    }

    /* access modifiers changed from: private */
    public boolean nameNotExist(String name) {
        String name2 = name.trim();
        if (name2.equals("")) {
            showDialogNameError(this.mResources.getString(R.string.toast_name_empty));
            return false;
        }
        Iterator<SpinnerItem> it = MainActivity.listAlbum.iterator();
        while (it.hasNext()) {
            if (it.next().getTitle().equals(name2)) {
                showDialogNameError(this.mResources.getString(R.string.toast_name_exist));
                return false;
            }
        }
        folderName = name2;
        return true;
    }

    private void showDialogNameError(String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(msg);
        dialog.setNegativeButton(this.mResources.getString(R.string.dialog_button_yes), (DialogInterface.OnClickListener) null);
        dialog.setCancelable(false);
        dialog.show();
    }

    /* access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void goToGellaryMultiSelect(String albumName) {
        if (Build.VERSION.SDK_INT < 23) {
            goToGallary(albumName);
        } else if (getActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != 0) {
            requestPermission();
        } else {
            goToGallary(albumName);
        }
    }

    private void goToGallary(String albumName) {
        Intent intent = new Intent(getActivity(), MultiSelectImagesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.UserFolderName, albumName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void requestPermission() {
        if (FragmentCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            showDialogRequestPermission();
            return;
        }
        FragmentCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
    }

    private void showToast(final String sms) {
        final Activity activity = getActivity();
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                Toast.makeText(activity, sms, 0).show();
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void setWallpaper() throws IOException {
        ArrayList<String> arrayList = null;
        int numberItemSelected = 0;
        try {
            arrayList = imageCustomAdapter.getCheckedItem();
            numberItemSelected = arrayList.size();
        } catch (Exception e) {
            e.printStackTrace();
            if (arrayList == null) {
                Log.e(TAG, "arrayList null ");
            }
        }
        Toast.makeText(getActivity(), numberItemSelected + " " + this.mResources.getString(R.string.toast_selected), 0).show();
        if (numberItemSelected != 0) {
            this.pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), WallpaperReceiver.class), 536870912);
            if (this.pendingIntent != null) {
                this.manager.cancel(this.pendingIntent);
                Log.e(TAG, "canceled pendingIntent");
            }
            if (numberItemSelected == 1) {
                int rotaDe = rotationDegree(new ExifInterface(arrayList.get(0)).getAttributeInt("Orientation", 1));
                Matrix matrix = new Matrix();
                matrix.postRotate((float) rotaDe);
                Bitmap bitmap = decodeBitmapFile(arrayList.get(0));
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap2 != null) {
                    new SetWallpaper(0).execute(new Bitmap[]{bitmap2});
                    return;
                }
                Log.e(TAG, "null");
            } else if (numberItemSelected > 1) {
                try {
                    setArray(arrayList, Const.WallpaperSet);
                    this.pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), WallpaperReceiver.class), 0);
                    new SetWallpaper(1).execute(new Bitmap[1]);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
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

    private Bitmap decodeBitmapFile(String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = 1;
        while (options.inSampleSize <= 32) {
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

    private static void setArray(ArrayList<String> field, String key) {
        SharedPreferences.Editor editor = pref.edit();
        JSONArray jsArray = new JSONArray();
        if (field.size() > 0) {
            for (int i = 0; i < field.size(); i++) {
                jsArray.put(field.get(i));
            }
            editor.putString(key, jsArray.toString());
        } else {
            editor.putString(key, (String) null);
        }
        editor.commit();
        Log.e(TAG, "save field");
    }

    private class SetWallpaper extends AsyncTask<Bitmap, Integer, String> {
        int typeSet;

        public SetWallpaper(int type) {
            this.typeSet = type;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            Your_Album_Fragment.this.setWallDialog.show();
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public String doInBackground(Bitmap... bitmaps) {
            Your_Album_Fragment.this.saveToPref(Const.Type, "Custom");
            Your_Album_Fragment.this.saveToPref(Const.KeyField, "C" + Your_Album_Fragment.customItemSellected);
            Your_Album_Fragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Your_Album_Fragment.this.saveIntToPref(Const.TimeToChange, Your_Album_Fragment.cSpinnerTime.getSelectedItemPosition());
                }
            });
            Your_Album_Fragment.this.saveSparseBooleanArray(Your_Album_Fragment.imageCustomAdapter.getBooleanArraySelected(), Const.keyArrayBooleanItemSelected);
            try {
                if (this.typeSet == 0) {
                    Bitmap bm_scale = bitmaps[0];
                    if (Your_Album_Fragment.pref.getBoolean(Const.Auto_Crop, false)) {
                        bm_scale = Bitmap.createScaledBitmap(bitmaps[0], Your_Album_Fragment.this.width, Your_Album_Fragment.this.height, true);
                    }
                    Your_Album_Fragment.this.wallpaperManager.setBitmap(bm_scale);
                }
                if (this.typeSet != 1) {
                    return null;
                }
                Your_Album_Fragment.this.manager.setRepeating(0, System.currentTimeMillis(), (long) Your_Album_Fragment.timeInterval, Your_Album_Fragment.this.pendingIntent);
                Thread.sleep(2000);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /* access modifiers changed from: protected */
        @SuppressLint("WrongConstant")
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(Your_Album_Fragment.this.getActivity(), Your_Album_Fragment.this.mResources.getString(R.string.toast_set_complete), 0).show();
            Your_Album_Fragment.this.setWallDialog.dismiss();
        }
    }

    private static ArrayList<String> getArray(String key) {
        Log.e("getArray", "get fields");
        String data = pref.getString(key, (String) null);
        ArrayList<String> list = new ArrayList<>();
        if (data != null) {
            try {
                JSONArray jsArray = new JSONArray(data);
                for (int i = 0; i < jsArray.length(); i++) {
                    list.add(jsArray.optString(i));
                    Log.e(TAG, jsArray.optString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("getArray", "listfield: null");
        }
        return list;
    }

    private static void saveField(ArrayList<SpinnerItem> list, String key) {
        String json = new Gson().toJson((Object) list);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, json);
        editor.commit();
    }

    /* access modifiers changed from: private */
    public void saveToPref(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /* access modifiers changed from: private */
    public void saveIntToPref(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /* access modifiers changed from: private */
    public void saveSparseBooleanArray(SparseBooleanArray list, String key) {
        String json = new Gson().toJson((Object) list);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, json);
        editor.commit();
    }

    private SparseBooleanArray getBooleanArraySaved(String key) {
        Gson gson = new Gson();
        String json = pref.getString(key, (String) null);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        if (json != null) {
            return (SparseBooleanArray) gson.fromJson(json, SparseBooleanArray.class);
        }
        return sparseBooleanArray;
    }

    public void animateFAB() {
        if (this.isFabOpen) {
            this.fab.startAnimation(this.rotate_backward);
            this.fab1.startAnimation(this.fab_close);
            this.fab2.startAnimation(this.fab_close);
            this.fab3.startAnimation(this.fab_close);
            this.fab1.setClickable(false);
            this.fab2.setClickable(false);
            this.fab3.setClickable(false);
            this.txtFab1.startAnimation(this.text_fab_close);
            this.txtFab2.startAnimation(this.text_fab_close);
            this.txtFab3.startAnimation(this.text_fab_close);
            this.isFabOpen = false;
            Log.d(TAG, "fab close");
            return;
        }
        this.fab.startAnimation(this.rotate_forward);
        this.fab1.startAnimation(this.fab_open);
        this.fab2.startAnimation(this.fab_open);
        this.fab3.startAnimation(this.fab_open);
        this.fab1.setClickable(true);
        this.fab2.setClickable(true);
        this.fab3.setClickable(true);
        this.txtFab1.startAnimation(this.text_fab_open);
        this.txtFab2.startAnimation(this.text_fab_open);
        this.txtFab3.startAnimation(this.text_fab_open);
        this.isFabOpen = true;
        Log.d(TAG, "fab open");
    }
}
