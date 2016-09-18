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

            askForPermission(activity, Manifest.permission.READ_PHONE_STATE, new String[]{Manifest.permission.READ_PHONE_STATE});

            askForPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED});

            askForPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);


        }
    }

    private void askForPermission(Activity activity, String permissionName, String[] permissions) {
        int isGranted = ContextCompat.checkSelfPermission(activity, permissionName);

        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }
    }
    private void askForPermission(Activity activity, String permissionName) {
        int isGranted = ContextCompat.checkSelfPermission(activity, permissionName);

        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permissionName}, 1);
        }
    }
}
