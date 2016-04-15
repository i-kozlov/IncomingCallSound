package com.baybaka.notificationlib;

public interface NotificationSettings {

    boolean isUsingFlash();

    void setUsingFlash(boolean newValue);

    boolean isUseLed();

    void setUseLed(boolean newValue);

    String all();


    boolean isProVersion();
}
