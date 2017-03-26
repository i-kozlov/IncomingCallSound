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

    companion object Version{
        fun sdk() = Build.VERSION.SDK_INT

        fun noSecurityCheck(): Boolean = isAndroid5AndLower()
        /**
         * noSecurityCheck
         */
        fun isAndroid5AndLower(): Boolean = sdk() < Build.VERSION_CODES.M

        fun hasSecurityCheck(): Boolean = eqOrGreaterAndroid6()

        fun isAndroid6(): Boolean = sdk() == Build.VERSION_CODES.M
        fun eqOrGreaterAndroid6(): Boolean = sdk() >= Build.VERSION_CODES.M
        fun android7plus(): Boolean = sdk() >= Build.VERSION_CODES.N

        val dndReqCode = 1516
    }

    fun checkPhonePermissionsAndAsk() {
        if (hasSecurityCheck()) {
            askForPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.READ_PHONE_STATE)
        }
    }

    @SuppressLint("InlinedApi")
    fun checkMusicAndAsk(): Boolean = when {
        noSecurityCheck() -> true
        else -> {
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS)
        }
    }

    @TargetApi(23)
    fun askForDndPermission(): Boolean {
        val notificationManager: NotificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
//        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
    private fun askForPermission(vararg permissions: String): Boolean {

        return if (permissions.all { isGranted(it) }) true
        else {
            val activity = (context as? Activity)
//            activity?.requestPermissions(arrayOf(permissions))

            activity?.let { ActivityCompat.requestPermissions(it, permissions, 1) }
            return permissions.all { isGranted(it) }
        }

    }
}
