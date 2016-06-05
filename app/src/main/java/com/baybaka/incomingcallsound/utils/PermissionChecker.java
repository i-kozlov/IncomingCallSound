package com.baybaka.incomingcallsound.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionChecker {
    public PermissionChecker(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission_READ_PHONE_STATE = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_PHONE_STATE);
            if (permission_READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }

            int permission_RECEIVE_BOOT_COMPLETED = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED);
            if (permission_RECEIVE_BOOT_COMPLETED != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                        1);
            }
        }
    }
}
