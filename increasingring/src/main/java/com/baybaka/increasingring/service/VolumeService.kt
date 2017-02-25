package com.baybaka.increasingring.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.baybaka.increasingring.Injector
import com.baybaka.increasingring.RunTimeSettings
import com.baybaka.increasingring.SettingsService
import com.baybaka.increasingring.receivers.IncomingCallReceiver
import org.slf4j.LoggerFactory
import javax.inject.Inject

class VolumeService : Service() {

    private val LOG = LoggerFactory.getLogger(VolumeService::class.java.simpleName)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LOG.info("running service onStartCommand")
        LOG.info("args are $intent , $flags, $startId")
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @Inject
    internal lateinit var settings: SettingsService
    @Inject
    internal lateinit var mPhoneListener: PhoneStateListener
    @Inject
    internal lateinit var mRunTimeSettings: RunTimeSettings

    override fun onCreate() {
        super.onCreate()

        LOG.debug("VolumeService on create")

//        if (mPhoneListener == null) {

            val injector = applicationContext as Injector
            injector.inject(this)

            //            mPhoneListener = new MyPhoneStateListener(new Controller(mSettings, AudioManagerRealImpl.getInstance()));

            // Register listener for LISTEN_CALL_STATE
            telephonyManagerToRegisterListener.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE)
            mRunTimeSettings.setListenerIsRegistered()

            //            unregReceiver();


            startForegroundIfEnabled()
            LOG.debug("VolumeService is registered now")
//        }
    }

    private fun startForegroundIfEnabled() {

        if (settings.startInForeground() && settings.isServiceEnabledInConfig) {
            startForeground(4815, mRunTimeSettings.persistentNotification(this, settings.showNotificationWithMinPriority()))
        }
    }


    override fun onDestroy() {
        LOG.debug("VolumeService onDestroy")

        // Register listener for Stop listening for updates.
        telephonyManagerToRegisterListener.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE)

        //        mPhoneListener = null;
        //            regReceiver();

        mRunTimeSettings.setListenerUnregistered()
        stopForeground(true)
        super.onDestroy()

    }

    private val telephonyManagerToRegisterListener: TelephonyManager
        get() = applicationContext
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    private fun unregReceiver() {
        val pm = packageManager
        val compName = ComponentName(applicationContext,
                IncomingCallReceiver::class.java)
        pm.setComponentEnabledSetting(
                compName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }
}
