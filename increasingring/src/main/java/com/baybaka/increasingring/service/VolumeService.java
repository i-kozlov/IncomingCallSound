package com.baybaka.increasingring.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.baybaka.increasingring.Injector;
import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.receivers.IncomingCallReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class VolumeService extends Service {

    private Logger LOG = LoggerFactory.getLogger(VolumeService.class.getSimpleName());

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Inject
    SettingsService mSettingsService;
    @Inject
    PhoneStateListener mPhoneListener;
    @Inject
    RunTimeSettings mRunTimeSettings;

    @Override
    public void onCreate() {
        super.onCreate();

        LOG.debug("VolumeService on create");

        if (mPhoneListener == null) {
            //Create Listner
            Injector injector = (Injector)getApplicationContext();
            injector.inject(this);
//            mPhoneListener = new MyPhoneStateListener(new Controller(mSettings, AudioManagerRealImpl.getInstance()));

            // Register listener for LISTEN_CALL_STATE
            getTelephonyManagerToRegisterListener().listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

            mRunTimeSettings.setListenerIsRegistered();

//            unregReceiver();


            startForegroundIfEnabled();
            LOG.debug("VolumeService is registered now");
        }
    }

    private void startForegroundIfEnabled() {

        if (mSettingsService.startInForeground() && mSettingsService.isServiceEnabledInConfig()) {
            startForeground(4815, mRunTimeSettings.persistentNotification(this, mSettingsService.showNotificationWithMinPriority()));
        }
    }


    @Override
    public void onDestroy() {
        LOG.debug("VolumeService onDestroy");

        // Register listener for Stop listening for updates.
        getTelephonyManagerToRegisterListener().listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);

//        mPhoneListener = null;
        //            regReceiver();

        mRunTimeSettings.setListenerUnregistered();
        stopForeground(true);
        super.onDestroy();

    }

    private TelephonyManager getTelephonyManagerToRegisterListener() {
        return (TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    private void unregReceiver() {
        PackageManager pm = getPackageManager();
        ComponentName compName =
                new ComponentName(getApplicationContext(),
                        IncomingCallReceiver.class);
        pm.setComponentEnabledSetting(
                compName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
