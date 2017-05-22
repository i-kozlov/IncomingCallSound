package com.baybaka.increasingring.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.media.AudioManager
import com.baybaka.increasingring.utils.PermissionChecker
import com.baybaka.increasingring.utils.PermissionChecker.Version

class SettingsAdapter(val prefs: SettingsService, application: Application) : SettingsService by prefs {

    val doNotCheckDnD = PermissionChecker.isAndroid5AndLower()
    val checker by lazy { PermissionChecker(application) }

    override fun isServiceEnabledInConfig(): Boolean {

        //todo add scheduler usage
        return prefs.isServiceEnabledInConfig
    }

    override fun isSkipRing(): Boolean =
            prefs.isSkipRing && (this.isMuteFirst || this.isVibrateFirst)

    @SuppressLint("NewApi")
    private fun canUseMuteVibrate(): Boolean {
        return doNotCheckDnD|| Version.isAndroid6() || checker.doNotDisturbGranted()
    }

    @SuppressLint("NewApi")
    override fun canUseMusicStream(): Boolean {
        return Version.noSecurityCheck() || checker.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun isVibrateFirst(): Boolean {
        return prefs.isVibrateFirst && canUseMuteVibrate()
    }

    override fun isMuteFirst(): Boolean {
        return prefs.isMuteFirst && canUseMuteVibrate()
    }

    private val STREAM_RING = AudioManager.STREAM_RING
    private val STREAM_MUSIC = AudioManager.STREAM_MUSIC

    override fun getSoundStream(): Int =
            if (prefs.soundStream == STREAM_MUSIC && canUseMusicStream()) STREAM_MUSIC
//            else if (prefs.soundStream == STREAM_RING) STREAM_RING
            else STREAM_RING
}