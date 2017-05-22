package com.baybaka.increasingring.settings

import android.app.Notification
import android.content.Context
import com.baybaka.increasingring.interfaces.NotificationProvider

interface RunTimeSettings {

    val isLoggingEnabled: Boolean

    fun setListenerIsRegistered()

    fun setListenerUnregistered()

    fun configurationChanged()

    fun configIsUpdated()

    val isConfigChanged: Boolean

    val context: Context

    //todo notification should be move out
    fun persistentNotification(context: Context, minPriority: Boolean): Notification

    fun errorNotification(context: Context)

    fun findPhoneNotification()

    /**

     * @return is service running and listener registered
     */
    val isPhoneStateListenerRegistered: Boolean

    var isTestPageOpened: Boolean

    fun setGlobalLoggingState(state: Boolean)

    fun getNotifyProvider() : NotificationProvider
}
