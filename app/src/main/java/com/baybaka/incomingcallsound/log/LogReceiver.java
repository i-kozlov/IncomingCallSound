package com.baybaka.incomingcallsound.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baybaka.incomingcallsound.MyApp;

public class LogReceiver extends BroadcastReceiver {
    public static final String LOG_RESP = "com.baybaka.incomingcallsound.receiver.logger";
    private static final String MESSAGE = "message";

    public interface LogReceiveInterface{
        void logsToTextView(String logs);
    }

    private LogReceiveInterface fragment;

    public LogReceiver(LogReceiveInterface textView) {
        fragment = textView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (fragment != null && intent != null) {
            if (LOG_RESP.equals(intent.getAction())) {
                fragment.logsToTextView(intent.getStringExtra(MESSAGE));
            }
        }
    }

    public static void sendBroadcastToLogReceiver(String message) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LogReceiver.LOG_RESP);
        broadcastIntent.addCategory(LogReceiver.LOG_RESP);
        broadcastIntent.putExtra(MESSAGE, message);
        MyApp.getContext().sendBroadcast(broadcastIntent);
    }
}
