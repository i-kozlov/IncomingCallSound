package com.baybaka.increasingring.service

import android.content.Context
import android.content.Intent
import com.baybaka.increasingring.di.Getter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ServiceStarter {
    fun logger(): Logger = LoggerFactory.getLogger(ServiceStarter::class.java.simpleName)

    fun startServiceWithCondition(context: Context) {
        if (checkIsServiceEnabledInConfig(context)) {
            context.startService(Intent(context, VolumeService::class.java))
        }
    }

    private fun checkIsServiceEnabledInConfig(context: Context): Boolean {
        val settingsService = (context.applicationContext as Getter).setting
        val result = settingsService.isServiceEnabledInConfig
        logger().info("Service config status is {}: ", result)
        return result
    }

    fun stopServiceRestartIfEnabled(context: Context) {
        logger().info("Stopping service")
        context.stopService(Intent(context, VolumeService::class.java))
        startServiceWithCondition(context)
    }
}
