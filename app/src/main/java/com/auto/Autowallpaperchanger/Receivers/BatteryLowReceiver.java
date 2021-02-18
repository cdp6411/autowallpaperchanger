package com.auto.Autowallpaperchanger.Receivers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.auto.Autowallpaperchanger.Consts.Const;
import com.auto.Autowallpaperchanger.MainActivity;
import com.auto.Autowallpaperchanger.R;

public class BatteryLowReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (context.getSharedPreferences(Const.Pref, 0).getBoolean(Const.Check_Pause_Slide_Show, false)) {
            @SuppressLint("WrongConstant") AlarmManager manager = (AlarmManager) context.getSystemService("alarm");
            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, WallpaperReceiver.class), 536870912);
            @SuppressLint("WrongConstant") NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context).setSmallIcon(R.drawable.icon).setContentTitle(context.getString(R.string.app_name)).setAutoCancel(true).setDefaults(-1).setContentText(context.getString(R.string.notification_battery_low));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            Intent resultIntent = new Intent(context, MainActivity.class);
            stackBuilder.addParentStack((Class<?>) MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            mBuilder.setContentIntent(stackBuilder.getPendingIntent(0, 134217728));
            if (pendingIntent != null) {
                manager.cancel(pendingIntent);
                Log.d("SettingActivity", "Battery low: canceled receiver");
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }
}
