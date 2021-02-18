package com.auto.Autowallpaperchanger.Fragments;

import android.annotation.SuppressLint;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.auto.Autowallpaperchanger.Adapters.ImageAdapter;
import com.auto.Autowallpaperchanger.Consts.Const;
import com.auto.Autowallpaperchanger.R;
import com.auto.Autowallpaperchanger.Receivers.WallpaperReceiver;


import org.json.JSONArray;

import java.util.ArrayList;

public class Available_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static String TAG = "Available_Fragment";
    public static Button btSetWallpaper;
    private AdView mAdView;
    public static LinearLayout spinner_available;
    public static ImageAdapter imgAdapter;
    private static int[] mThumbIdsCar = {R.drawable.car1, R.drawable.car2, R.drawable.car3, R.drawable.car4, R.drawable.car5, R.drawable.car6, R.drawable.car7, R.drawable.car8, R.drawable.car9, R.drawable.car10, R.drawable.car11, R.drawable.car12,R.drawable.abeer, R.drawable.adjoining, R.drawable.affair, R.drawable.aimed, R.drawable.amiya,  R.drawable.anderson, R.drawable.andrew,  R.drawable.anyways,  R.drawable.arivunambi, R.drawable.arsenal, R.drawable.bans, R.drawable.beauty, R.drawable.belts,R.drawable.bilder,  R.drawable.black,  R.drawable.cabernet, R.drawable.caucus, R.drawable.certainty};
   private static int[] mThumbIdsSpace={R.drawable.space1,R.drawable.space2,R.drawable.space3,R.drawable.space4,R.drawable.space5,R.drawable.space6,R.drawable.space7,R.drawable.space8,R.drawable.space9,R.drawable.space10,R.drawable.space11,R.drawable.space12,R.drawable.accents,R.drawable.airplane,R.drawable.arden,R.drawable.bernard,R.drawable.capsule,R.drawable.chemistry,R.drawable.compelling,R.drawable.corning,R.drawable.cypress,R.drawable.denial,R.drawable.downs,R.drawable.eleanor,R.drawable.estonia,R.drawable.expenses};
    private static int[] mThumbIdsNature = {R.drawable.nature1, R.drawable.nature2, R.drawable.nature3, R.drawable.nature4, R.drawable.nature5, R.drawable.nature6, R.drawable.nature7, R.drawable.nature8, R.drawable.nature9, R.drawable.nature10, R.drawable.nature11, R.drawable.nature12};
    private static int  [] mThumbIdsAnimals ={R.drawable.abhi,R.drawable.acanda,R.drawable.acres,R.drawable.aleem,R.drawable.aura,R.drawable.bathing,R.drawable.bookstores,R.drawable.cette,R.drawable.coast,R.drawable.democratic,R.drawable.dismissal,R.drawable.doctorate,R.drawable.elango,R.drawable.enzyme,R.drawable.este,R.drawable.evolving,R.drawable.fakes,R.drawable.fest};
    public static Spinner spinner_time;
    /* access modifiers changed from: private */
    public static int timeInterval;
    private Bitmap bitmap;
    /* access modifiers changed from: private */
    public String fieldSelected;
    private GridView gridView;
    /* access modifiers changed from: private */
    public int height;
    /* access modifiers changed from: private */
    public AlarmManager manager;
    /* access modifiers changed from: private */
    public PendingIntent pendingIntent;
    /* access modifiers changed from: private */
    public SharedPreferences pref;
    /* access modifiers changed from: private */
    public Resources resources;
    private View rootView;
    /* access modifiers changed from: private */
    public ProgressDialog setWallDialog;
    /* access modifiers changed from: private */
    public WallpaperManager wallpaperManager;
    /* access modifiers changed from: private */
    public int width;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.available_layout, container, false);
        mAdView =this.rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        btSetWallpaper = (Button) this.rootView.findViewById(R.id.a_bt_set);
        btSetWallpaper.setOnClickListener(this);
        btSetWallpaper.setEnabled(false);
        spinner_available = (LinearLayout)rootView.findViewById(R.id.spinner_available);
        this.resources = getResources();
        spinner_time = (Spinner) this.rootView.findViewById(R.id.a_spinner_time);
        spinner_time.setEnabled(false);
        spinner_time.setOnItemSelectedListener(this);
        this.gridView = (GridView) this.rootView.findViewById(R.id.a_gridview);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.width = metrics.widthPixels;
        this.height = metrics.heightPixels;
        this.gridView.setColumnWidth(this.width / 4);
        this.manager = (AlarmManager) getActivity().getSystemService("alarm");
        this.wallpaperManager = WallpaperManager.getInstance(getActivity());
        this.pref = getActivity().getSharedPreferences(Const.Pref, 0);
        Bundle bundle = getArguments();
        this.fieldSelected = Const.AVAILABLE_FIELD_SELECTED;
        if (bundle != null) {
            this.fieldSelected = bundle.getString(Const.AVAILABLE_FIELD_SELECTED, Const.AVAILABLE_FIELD_SELECTED);
        }
        setAvailableGellary();
        this.setWallDialog = new ProgressDialog(getActivity());
        this.setWallDialog.setMessage(this.resources.getString(R.string.dialog_setting_sms));
        this.setWallDialog.setCancelable(false);
        setHasOptionsMenu(true);


        return this.rootView;

    }

    public void onPrepareOptionsMenu(Menu menu) {
        ((CheckBox) menu.getItem(0).getActionView()).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (Available_Fragment.imgAdapter != null) {
                    Available_Fragment.imgAdapter.selectAll(checked);
                }
            }
        });
        super.onPrepareOptionsMenu(menu);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity, menu);
    }

    @SuppressLint("WrongConstant")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a_bt_set /*2131558519*/:
                ArrayList<Integer> arrayList = imgAdapter.getItemSelectedList();
                int numberItemSelected = arrayList.size();
                Toast.makeText(getActivity(), numberItemSelected + " " + this.resources.getString(R.string.toast_selected), 0).show();
                if (numberItemSelected != 0) {
                    this.pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), WallpaperReceiver.class), 536870912);
                    if (this.pendingIntent != null) {
                        this.manager.cancel(this.pendingIntent);
                        Log.e(TAG, "canceled receiver");
                    }
                    if (numberItemSelected == 1) {
                        this.bitmap = decodeBitmapResource(getActivity(), arrayList.get(0).intValue());
                        if (this.bitmap != null) {
                            new SetWallpaper(0).execute(new Bitmap[]{this.bitmap});
                            return;
                        }
                        return;
                    } else if (numberItemSelected > 1) {
                        try {
                            setArrayImageToPref(arrayList, Const.WallpaperSet);
                            this.pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), WallpaperReceiver.class), 0);
                            new SetWallpaper(1).execute(new Bitmap[1]);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
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

    private class SetWallpaper extends AsyncTask<Bitmap, Integer, String> {
        int typeSet;

        public SetWallpaper(int type) {
            this.typeSet = type;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            Available_Fragment.this.setWallDialog.show();
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"WrongConstant", "MissingPermission"})
        public String doInBackground(Bitmap... bitmaps) {
            Available_Fragment.this.saveToPref(Const.Type, "Available");
            Available_Fragment.this.saveToPref(Const.KeyField, "A" + Available_Fragment.this.fieldSelected);
            Available_Fragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Available_Fragment.this.saveIntToPref(Const.TimeToChange, Available_Fragment.spinner_time.getSelectedItemPosition());
                }
            });
            Available_Fragment.this.saveSparseBooleanArray(Available_Fragment.imgAdapter.getBooleanArraySelected(), Const.keyArrayBooleanItemSelected);
            try {
                if (this.typeSet == 0) {
                    Bitmap bm_scale = bitmaps[0];
                    if (Available_Fragment.this.pref.getBoolean(Const.Auto_Crop, false)) {
                        bm_scale = Bitmap.createScaledBitmap(bitmaps[0], Available_Fragment.this.width, Available_Fragment.this.height, true);
                    }
                    Available_Fragment.this.wallpaperManager.setBitmap(bm_scale);
                }
                if (this.typeSet != 1) {
                    return null;
                }
                Available_Fragment.this.manager.setRepeating(0, System.currentTimeMillis(), (long) Available_Fragment.timeInterval, Available_Fragment.this.pendingIntent);
                Thread.sleep(2000);
                return null;
            } catch (Exception e) {
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
            Toast.makeText(getActivity(), "Wallpaper is set after 1 minute", Toast.LENGTH_SHORT).show();
            Toast.makeText(Available_Fragment.this.getActivity(), Available_Fragment.this.resources.getString(R.string.toast_set_complete), 0).show();
            Available_Fragment.this.setWallDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void saveSparseBooleanArray(SparseBooleanArray list, String key) {
        String json = new Gson().toJson((Object) list);
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putString(key, json);
        editor.commit();
    }

    private SparseBooleanArray getBooleanArraySaved(String key) {
        Gson gson = new Gson();
        String json = this.pref.getString(key, (String) null);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        if (json != null) {
            return (SparseBooleanArray) gson.fromJson(json, SparseBooleanArray.class);
        }
        return sparseBooleanArray;
    }

    /* access modifiers changed from: private */
    public void saveToPref(String key, String value) {
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /* access modifiers changed from: private */
    public void saveIntToPref(String key, int value) {
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putInt(key, value);
        editor.commit();
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
        }




    }

    private int[] getThumbField(String field) {
        char c = 65535;
        switch (field) {
            case Const.AVAILABLE_FIELD_SELECTED:
                if (field.equals(Const.AVAILABLE_FIELD_SELECTED)) {
                    c = 0;
                    break;
                }
                break;
            case "Car":
                if (field.equals("Car")) {
                    c = 2;
                    break;
                }
                break;
            case "Space":
                if (field.equals("Space")) {
                    c = 1;
                    break;
                }
                break;
            case "Animals":
                if (field.equals("Animals")){
                    c=3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return mThumbIdsNature;
            case 1:
                return mThumbIdsSpace;
            case 2:
                return mThumbIdsCar;
            case 3:
                return mThumbIdsAnimals;
            default:
                return null;
        }
    }

    private Bitmap decodeBitmapResource(Context context, int resource) {
        Bitmap bitmap2 = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        while (options.inSampleSize <= 32) {
            try {
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), resource, options);
                Log.e(TAG, "decode successfull: " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "OutOfMemoryError: " + options.inSampleSize + "-" + resource);
                options.inSampleSize++;
            }
        }
        return bitmap2;
    }

    private void setArrayImageToPref(ArrayList<Integer> arrIds, String key) {
        SharedPreferences.Editor editor = this.pref.edit();
        JSONArray jsArray = new JSONArray();
        if (arrIds.size() > 0) {
            for (int i = 0; i < arrIds.size(); i++) {
                jsArray.put(arrIds.get(i));
            }
            editor.putString(key, jsArray.toString());
        } else {
            editor.putString(key, (String) null);
        }
        editor.commit();
    }

    private void setAvailableGellary() {
        Log.d(TAG, this.fieldSelected);
        if (this.pref.getString(Const.KeyField, "").equals("A" + this.fieldSelected)) {
            imgAdapter = new ImageAdapter(getActivity(), getThumbField(this.fieldSelected), getBooleanArraySaved(Const.keyArrayBooleanItemSelected));
            spinner_time.setSelection(this.pref.getInt(Const.TimeToChange, 0));
        } else {
            imgAdapter = new ImageAdapter(getActivity(), getThumbField(this.fieldSelected), (SparseBooleanArray) null);
            spinner_time.setSelection(0);
        }
        this.gridView.setAdapter(imgAdapter);
        int count = imgAdapter.getCounSelected();
        if (count > 0) {
            btSetWallpaper.setEnabled(true);
        } else {
            btSetWallpaper.setEnabled(false);
        }
        if (count > 1) {
            spinner_time.setEnabled(true);
        } else {
            spinner_time.setEnabled(false);
        }
    }
}
