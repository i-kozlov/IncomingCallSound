package com.baybaka.increasingring.config

import android.media.AudioManager
import com.baybaka.increasingring.settings.SettingsService

class RingerConfig {
    var startSoundLevel: Int = 0
    var interval: Int = 5
    var allowedMaxVolume: Int = 0

    var isVibrateFirst: Boolean = false
    var vibrateTimes: Int = 0

    var isMuteFirst: Boolean = false
    var muteTimes: Int = 0

    var doNotRing = false
    var useMusicStream: Boolean = false

    override fun toString(): String {
        return "RingerConfig(startSoundLevel=$startSoundLevel, interval=$interval, allowedMaxVolume=$allowedMaxVolume, isVibrateFirst=$isVibrateFirst, vibrateTimes=$vibrateTimes, isMuteFirst=$isMuteFirst, muteTimes=$muteTimes, useMusicStream=$useMusicStream)"
    }

    fun update(settingsService: SettingsService) {
        updateStream(settingsService.soundStream)

        interval = settingsService.interval

        isMuteFirst = settingsService.isMuteFirst
        muteTimes = settingsService.muteTimesCount

        isVibrateFirst = settingsService.isVibrateFirst
        vibrateTimes = settingsService.vibrateTimesCount

        doNotRing = settingsService.isSkipRing
    }

    private fun updateStream(soundStream: Int) {
        useMusicStream = soundStream == AudioManager.STREAM_MUSIC
    }
}


