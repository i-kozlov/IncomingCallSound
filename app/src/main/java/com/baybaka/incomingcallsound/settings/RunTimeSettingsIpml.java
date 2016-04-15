package com.baybaka.incomingcallsound.settings;

import android.app.Notification;
import android.content.Context;

import com.baybaka.incomingcallsound.BuildConfig;
import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.utils.NotificationFactory;
import com.baybaka.increasingring.RunTimeSettings;

public class RunTimeSettingsIpml implements RunTimeSettings {

    private static volatile boolean testPageOpened = false;
    private static volatile boolean configChanged = false;
    private static volatile boolean globalLogging = false;
    private static volatile boolean phoneStateListenerRegistered = false;

    private long lastVolumeButtonClickTime = 0;

    public RunTimeSettingsIpml() {
        globalLogging = MyApp.get().getSetting().isLoggingEnabled();
    }

    @Override
    public boolean isLoggingEnabled() {
        return BuildConfig.DEBUG || testPageOpened || globalLogging;
    }

    @Override
    public void setListenerIsRegistered() {
        phoneStateListenerRegistered = true;
    }

    @Override
    public void setListenerUnregistered() {
        phoneStateListenerRegistered = false;
    }

    @Override
    public boolean isPhoneStateListenerRegistered() {
        return phoneStateListenerRegistered;
    }


    @Override
    public void configurationChanged() {
        configChanged = true;
    }

    @Override
    public void configIsUpdated() {
        configChanged = false;
    }

    @Override
    public boolean isConfigChanged() {
        return configChanged;
    }

    @Override
    public Context getContext() {
        return MyApp.getContext();
    }

    @Override
    public Notification persistentNotification(Context context, boolean minPriority) {
        return NotificationFactory.persistent(context, minPriority);
    }

    @Override
    public void errorNotification(Context context) {
        NotificationFactory.showMusicStreamErrotNotify(context);
    }

    @Override
    public void findPhoneNotification() {
        NotificationFactory.findPhone();
    }

    @Override
    public boolean isTestPageOpened() {
        return testPageOpened;
    }

    @Override
    public void setTestPageOpened(boolean state) {
        testPageOpened = state;
    }

    @Override
    public void setGlobalLoggingState(boolean state) {
        globalLogging = state;
    }

    @Override
    public long getLastVolActionTime() {
        return lastVolumeButtonClickTime;
    }

    @Override
    public void setLastVolActionTime(long mils) {
        lastVolumeButtonClickTime = mils;
    }
}
