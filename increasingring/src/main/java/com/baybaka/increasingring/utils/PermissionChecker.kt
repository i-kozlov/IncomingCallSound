package com.baybaka.increasingring.utils

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class PermissionChecker(val context: Context) {

    companion object {
        fun sdk() = Build.VERSION.SDK_INT

        fun noSecurityCheck(): Boolean = sdk() < Build.VERSION_CODES.M
        /**
         * noSecurityCheck
         */
        fun isAndroid5AndLower(): Boolean = sdk() < Build.VERSION_CODES.M

        fun isAndroid6(): Boolean = sdk() == Build.VERSION_CODES.M
        fun android7plus(): Boolean = sdk() >= Build.VERSION_CODES.N
        fun hasSecurityCheck(): Boolean = sdk() >= Build.VERSION_CODES.M

        val dndReqCode = 1516
    }

    fun checkPhonePermissionsAndAsk() {
        if (hasSecurityCheck()) {
            askForPermission(Manifest.permission.READ_PHONE_STATE)
            askForPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
        }
    }

    @SuppressLint("InlinedApi")
    fun checkMusicAndAsk(): Boolean = when {
        noSecurityCheck() -> true
        else -> {
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    askForPermission(Manifest.permission.READ_CONTACTS)

        }
    }

    @TargetApi(23)
    fun askForDndPermission(): Boolean {
//        val notificationManager: NotificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return when {
            hasSecurityCheck() && notificationManager.isNotificationPolicyAccessGranted -> true
            hasSecurityCheck() -> {

                (context as? Activity)?.let {
                    val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    it.startActivityForResult(intent, dndReqCode)
                }
                notificationManager.isNotificationPolicyAccessGranted
            }
            else -> true
        }
    }

    @TargetApi(23)
    fun doNotDisturbGranted(): Boolean {
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return when {
            hasSecurityCheck() && notificationManager.isNotificationPolicyAccessGranted -> true
            noSecurityCheck() -> true
            else -> false
        }
    }


    fun isGranted(permissionName: String) =
            ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED

    /*
    * Call from activity
    * */
    private fun askForPermission(permissionName: String): Boolean {

        return if (isGranted(permissionName)) true
        else {
            val activity = (context as? Activity)
//            activity?.requestPermissions(arrayOf(permissionName))

            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(permissionName), 1) }
            return isGranted(permissionName)
        }

    }
}
