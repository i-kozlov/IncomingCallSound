package com.baybaka.increasingring;

import android.app.Notification;
import android.content.Context;

public interface RunTimeSettings {

    boolean isLoggingEnabled();

    void setListenerIsRegistered();

    void setListenerUnregistered();

    void configurationChanged();

    void configIsUpdated();

    boolean isConfigChanged();

    Context getContext();

    Notification persistentNotification(Context context, boolean minPriority);

    void errorNotification(Context context);

    void findPhoneNotification();

    /**
     *
     * @return is service running and listener registered
     */
    boolean isPhoneStateListenerRegistered();

    boolean isTestPageOpened();

    void setTestPageOpened(boolean state);

    void setGlobalLoggingState(boolean state);


    //to del
    long getLastVolActionTime();

    void setLastVolActionTime(long mils);
}
