package com.auto.Autowallpaperchanger;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.gson.Gson;
import com.auto.Autowallpaperchanger.Adapters.SpinnerItemAdapter;
import com.auto.Autowallpaperchanger.Consts.Const;
import com.auto.Autowallpaperchanger.Fragments.Available_Fragment;
import com.auto.Autowallpaperchanger.Fragments.Your_Album_Fragment;
import com.auto.Autowallpaperchanger.models.SpinnerItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    private static final int ALBUM = 1;
    private static final int AVAILABLE = 2;
    private static String[] Fields = {Const.AVAILABLE_FIELD_SELECTED, "Space", "Car","Animals"};
    public static String TAG = "MainActivity";
    public static SpinnerItemAdapter albumAdapter;
    public static ProgressDialog createAlbumDialog;
    /* access modifiers changed from: private */
  //  public static InterstitialAd interAds;
    public static ArrayList<SpinnerItem> listAlbum;
    public static ArrayList<SpinnerItem> getAvailableList;
    public static DisplayImageOptions options;
    private static SharedPreferences pref;
    public static Spinner spinner_field;
    public static Context themeContext;
    private SpinnerItemAdapter availableAdapter;
    private int curNavItem;
    private String fieldSelected;
    private String[] fieldsList;
    private Menu mMenu;
    private Resources mResources;
    private int pos_item_album;
    private Resources resources;
    private InterstitialAd mInterstitialAd;


    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mResources = getResources();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        setTitle(navigationView.getMenu().getItem(0).getTitle());
        setAlbumleGellary();
        this.resources = getResources();
        pref = getSharedPreferences(Const.Pref, 0);
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_menu_image).showImageForEmptyUri(R.drawable.ic_menu_image_error).cacheInMemory().cacheOnDisc().build();
        themeContext = getSupportActionBar().getThemedContext();
        this.curNavItem = 1;
        spinner_field = (Spinner) findViewById(R.id.sp_available_field);
       spinner_field.setOnItemSelectedListener(this);
        updateSpinnerField();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        createAlbumDialog = new ProgressDialog(this);
        createAlbumDialog.setMessage(this.mResources.getString(R.string.dialog_create_new_album));
        createAlbumDialog.setCancelable(false);


    }


    @SuppressLint("WrongConstant")
    private void updateSpinnerField() {
        switch (this.curNavItem) {
            case 1:
                spinner_field.setVisibility(0);
                listAlbum = getField(Const.Custom_Field_Added);
                albumAdapter = new SpinnerItemAdapter(themeContext, 17367048, listAlbum);
                spinner_field.setAdapter(albumAdapter);
                return;
            case 2:
//                spinner_field.setVisibility(View.VISIBLE);
//                getAvailableList= getField(Const.AVAILABLE_FIELD_SELECTED);
//                getAvailableList=getField(Const.AVAILABLE_FIELD_SELECTED_CAR);
//                getAvailableList=getField(Const.AVAILABLE_FIELD_SELECTED_Universe);
//                availableAdapter=new SpinnerItemAdapter(themeContext,17367048,getAvailableList());
//                spinner_field.setAdapter(availableAdapter);
//                return;
                spinner_field.setVisibility(0);
                this.fieldsList = this.resources.getStringArray(R.array.available_wallpaper);
                this.availableAdapter = new SpinnerItemAdapter(themeContext, 17367048, getAvailableList());
                spinner_field.setAdapter(this.availableAdapter);
                return;
            default:
                return;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getId() == R.id.sp_available_field) {
            switch (this.curNavItem) {
                case 1:
                    this.pos_item_album = pos;
                    setAlbumleGellary();
                    return;
                case 2:
                    this.fieldSelected = Fields[pos];
                    setAvailableGellary();
                    return;
                default:
                    return;
            }
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.customdialog, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")

                .setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                        ImageView img = (ImageView)findViewById(R.id.imageofspleshscreen);
//                        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
//                        img.startAnimation(aniFade);
//                        img.setVisibility(View.VISIBLE);
//                        img.startAnimation(aniFade);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNeutralButton("Rate us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent inrate = new Intent(Intent.ACTION_VIEW, uri1);

                try {
                    startActivity(inrate);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdLoader.Builder builder1 = new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");
        builder1.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template =dialogView.findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });
        AdLoader adLoader = builder1.build();
        adLoader.loadAd(new AdRequest.Builder().build());

        builder.create();
        builder.show();

    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v11, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.app.Fragment} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
//    public boolean onNavigationItemSelected(android.view.MenuItem r12) {
//
//        throw new UnsupportedOperationException("Method not decompiled: com.vadev.MainActivity.onNavigationItemSelected(android.view.MenuItem):boolean");
//    }

    private void share() {
        Intent i = new Intent("android.intent.action.SEND");
        i.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.vadev");
        i.setType("text/plain");
        i.putExtra("android.intent.extra.SUBJECT", "Wallpaper Changer");
        startActivity(Intent.createChooser(i, getResources().getString(R.string.title_share)));
    }

    private ArrayList<SpinnerItem> getAvailableList() {
        ArrayList<SpinnerItem> list = new ArrayList<>();
        list.add(new SpinnerItem(R.drawable.icon_nature, this.fieldsList[0]));
        list.add(new SpinnerItem(R.drawable.icon_space, this.fieldsList[1]));
        list.add(new SpinnerItem(R.drawable.icon_car, this.fieldsList[2]));
        list.add(new SpinnerItem(R.drawable.abhi,this.fieldsList[3]));
        return list;
    }

    private void setAvailableGellary() {
        Fragment availableFg = new Available_Fragment();
        Bundle args = new Bundle();
        args.putString(Const.AVAILABLE_FIELD_SELECTED, this.fieldSelected);
        availableFg.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, availableFg).commit();
    }


    private void setAlbumleGellary() {
        Fragment albumFragment = new Your_Album_Fragment();
        Bundle args = new Bundle();
        args.putInt(Const.ALBUM_FIELD_SELECTED, this.pos_item_album);
        albumFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, albumFragment).commit();
    }

    private ArrayList<SpinnerItem> getField(String key) {
        Gson gson = new Gson();
        String json = pref.getString(key, (String) null);
        if (json != null) {
            return new ArrayList<>(Arrays.asList((SpinnerItem[]) gson.fromJson(json, SpinnerItem[].class)));
        }
        return new ArrayList<>();
    }








    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_your_album) {
            setAlbumleGellary();
            curNavItem=1;
            updateSpinnerField();

        } else if (id == R.id.nav_available) {
            setAvailableGellary();
            curNavItem=2;
            updateSpinnerField();


        } else if (id == R.id.nav_share) {
            share();

        }  else if (id == R.id.nav_setting) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            Intent intent = new Intent(this, SettingActivity.class);
            this.startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







    public static class MyReceiver extends BroadcastReceiver {
        @SuppressLint("WrongConstant")
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, context.getResources().getString(R.string.toast_create_album_complete), 0).show();
            MainActivity.listAlbum.add(new SpinnerItem(R.drawable.rsz_22logo, Your_Album_Fragment.folderName));
            MainActivity.albumAdapter.notifyDataSetChanged();
            MainActivity.saveField(MainActivity.listAlbum, Const.Custom_Field_Added);
            MainActivity.spinner_field.setSelection(MainActivity.listAlbum.size() - 1);
            MainActivity.createAlbumDialog.dismiss();
            MainActivity.spinner_field.setEnabled(true);
        }
    }

    /* access modifiers changed from: private */
    public static void saveField(ArrayList<SpinnerItem> list, String key) {
        String json = new Gson().toJson((Object) list);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, json);
        editor.commit();
    }

}
