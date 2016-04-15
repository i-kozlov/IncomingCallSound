package com.baybaka.increasingring.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TestPageOnCallEvenReceiver extends BroadcastReceiver {
    public static final String CALL_RECEIVER = "com.baybaka.increasingring.receiver.testpage.oncall";

    public interface FragmentCallUpdater {
        void update();
    }

    private FragmentCallUpdater linkToFragment;

    public TestPageOnCallEvenReceiver(FragmentCallUpdater testPageActivity) {
        linkToFragment = testPageActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (linkToFragment != null && intent!=null && CALL_RECEIVER.equals(intent.getAction()) ) {
            linkToFragment.update();
        }
    }

    public static void sendBroadcastToLogReceiver(Context context) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(CALL_RECEIVER);
        broadcastIntent.addCategory(CALL_RECEIVER);
        context.sendBroadcast(broadcastIntent);
    }
}
