package com.baybaka.increasingring.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.baybaka.increasingring.di.Getter
import com.baybaka.increasingring.service.ServiceStarter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class IncomingCallReceiver : BroadcastReceiver() {

    companion object{
        val LOG: Logger = LoggerFactory.getLogger(IncomingCallReceiver::class.java.simpleName)
    }


    override fun onReceive(context: Context, intent: Intent?) {

        val runTimeSettings = (context.applicationContext as Getter).runTimeSettings

        val registered = runTimeSettings.isPhoneStateListenerRegistered
        LOG.debug("IncomingCallReceiver onReceive. PhoneStateListener is registered = $registered")

        //service is killed - need to restart it
        if (!registered) {
            ServiceStarter.startServiceWithCondition(context)
        }

    }

}
