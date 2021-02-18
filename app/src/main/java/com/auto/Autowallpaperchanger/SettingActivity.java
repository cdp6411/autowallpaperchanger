package com.auto.Autowallpaperchanger;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.auto.Autowallpaperchanger.Consts.Const;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import static com.google.android.gms.ads.AdLoader.*;


public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
  //  private AdView adView;
    private LinearLayout banner_layout;
    private AlarmManager manager;
    private SwitchCompat switch_auto_crop;
    private SwitchCompat switch_battery_low;

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.setting_layout);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdLoader.Builder builder = new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template = findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());

        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            Toast.makeText(this, "null bar", 0).show();
        } else {
            bar.setDisplayHomeAsUpEnabled(true);
        }
        this.manager = (AlarmManager) getSystemService("alarm");
        this.switch_battery_low = (SwitchCompat) findViewById(R.id.setting_battery_low);
        SharedPreferences pref = getSharedPreferences(Const.Pref, 0);
        this.switch_battery_low.setChecked(pref.getBoolean(Const.Check_Pause_Slide_Show, false));
        this.switch_battery_low.setOnCheckedChangeListener(this);
        this.switch_auto_crop = (SwitchCompat) findViewById(R.id.setting_auto_crop);
        this.switch_auto_crop.setChecked(pref.getBoolean(Const.Auto_Crop, false));
        this.switch_auto_crop.setOnCheckedChangeListener(this);
        this.banner_layout = (LinearLayout) findViewById(R.id.seting_banner);
//        this.adView = new AdView(this);
//        this.adView.setAdSize(AdSize.BANNER);
//        this.adView.setAdUnitId(Const.Ads_Banner);
//        this.adView.loadAd(new AdRequest.Builder().build());
//        this.banner_layout.addView(this.adView);
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {
            case R.id.setting_auto_crop:
                saveToPref(Const.Auto_Crop, checked);
                return;
            case R.id.setting_battery_low:
                saveToPref(Const.Check_Pause_Slide_Show, checked);
                return;
            default:
                return;
        }
    }

    private void saveToPref(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(Const.Pref, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
