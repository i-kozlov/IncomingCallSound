package com.baybaka.incomingcallsound.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.main.MainActivity;

public class NotificationFactory {

    public static Notification persistent(Context context, boolean minPriority) {

        Intent intent = new Intent(context, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notifitationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getString(R.string.keep_in_memory_active_notification_text))
                .setContentIntent(pi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (minPriority) {
                notifitationBuilder.setPriority(Notification.PRIORITY_MIN);
            } else {
                notifitationBuilder.setPriority(Notification.PRIORITY_MAX);
            }
        }

        Notification note = notifitationBuilder.build();
        note.flags |= Notification.FLAG_NO_CLEAR;
        return note;
    }

    public static Notification disposable(Context context) {

        Intent intent = new Intent(context, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(updateMessage(context))
                .setContentText(context.getString(R.string.update_notify_message))
//                .setTicker(context.getString(R.string.ticker_onUpgrade))
                .setContentIntent(pi)
        ;
        return builder.build();
    }

    private static String updateMessage(Context context) {
        return context.getString(R.string.update_notify_message);
    }

    private static int getSmallIcon(){
        if (Build.VERSION.SDK_INT < 1) {
            return R.drawable.ic_launcher;
        }
        else {
            return R.drawable.ic_stat_notify_2;
        }
    }

    public static void showMusicStreamErrotNotify(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder
                .setSmallIcon(R.drawable.ic_report_problem_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(context.getString(R.string.lib_string_error_ititiazing_mediaplayer))
                .setContentText(context.getString(R.string.lib_string_error_ititiazing_mediaplayer))
                .setTicker(context.getString(R.string.lib_string_error_ititiazing_mediaplayer))
                .setContentIntent(pi)
        ;
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public static void findPhone() {
        Context context = MyApp.getContext();
        Intent intent = new Intent(context, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder
                .setSmallIcon(R.drawable.ic_report_problem_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(context.getString(R.string.find_phone_notification))
                .setContentText(context.getString(R.string.find_phone_notification))
                .setTicker(context.getString(R.string.find_phone_notification))
                .setContentIntent(pi)
        ;

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

}
