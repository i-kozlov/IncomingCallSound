package com.baybaka.increasingring.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionChecker(val activity: Activity) {

    fun checkMain(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askForPermission(activity, Manifest.permission.READ_PHONE_STATE, arrayOf(Manifest.permission.READ_PHONE_STATE))
            askForPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED, arrayOf(Manifest.permission.RECEIVE_BOOT_COMPLETED))
        }
    }

    fun checkMusic(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val forMusic = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE)
            askForPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, forMusic)
            askForPermission(activity, Manifest.permission.READ_CONTACTS, forMusic)
        }
        return true
    }

    private fun askForPermission(activity: Activity, permissionName: String, permissions: Array<String>): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(activity, permissionName)

        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, 1)
        }

        return isGranted != PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermission(activity: Activity, permissionName: String) {
        val isGranted = ContextCompat.checkSelfPermission(activity, permissionName)

        if (isGranted != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permissionName), 1)
        }
    }
}
