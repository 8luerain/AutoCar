package com.example.bluerain.carok.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by bluerain on 17-2-10.
 */

public class StarterReceiver extends BroadcastReceiver {
    private static final String TAG = "StarterReceiver";

    private NotificationManager mNotificationManager;

    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        sendNotification("hahaha");
    }

    private void sendNotification(String message) {
        final Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(mContext).setSmallIcon(android.R.drawable.ic_btn_speak_now).setTicker("testNote")
                    .setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                    .setContentTitle("title")
                    .setContentText(message)
                    .build();
            mNotificationManager.notify(1, notification);
        }
    }
}
