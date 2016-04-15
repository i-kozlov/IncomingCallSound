package com.baybaka.incomingcallsound.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpgradeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null || context == null || intent.getAction() == null) return;

//        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(2342, NotificationFactory.disposable(context));
//        }
    }
}
