package com.baybaka.increasingring.config

import android.media.AudioManager
import com.baybaka.increasingring.audio.IAudioController
import com.baybaka.increasingring.settings.SettingsService
import org.slf4j.LoggerFactory
import javax.inject.Inject

class CachingConfigFactory @Inject
constructor(private val settingsService: SettingsService,
            private val audio: IAudioController) {

    private val config: RingerConfig = RingerConfig()
    private var maxHardwareVolumeLevel: Int = 0
    private var oldPreRingLevel: Int = 0

    private var isMinVolLimited: Boolean = false
    private var isMinLimitToPreRing: Boolean = false
    private var minVolLimit: Int = 1

    private var isMaxVolLimited: Boolean = false
    private var isMaxLimitToPreRing: Boolean = false
    private var maxVolLimit: Int = 99


    private val LOG = LoggerFactory.getLogger(CachingConfigFactory::class.java.simpleName)


    fun updateMaxHardwareVolumeLevel() {
        this.maxHardwareVolumeLevel = audio.new_GetMaxLevel()
    }


    internal fun currentConfig(): RingerConfig {
        val preRingLevel = getCurrentChosenStreamVolume()

        if (preRingLevel != oldPreRingLevel) {
            if (isMinLimitToPreRing || isMaxLimitToPreRing) {
                updateMinMaxForConfig(preRingLevel)
            }
            oldPreRingLevel = preRingLevel
        }
        return config
    }

    fun getConfig(): RingerConfig {
        return config
    }


    internal val findPhoneConfig: RingerConfig
        get() = RingerConfig().apply {
            startSoundLevel = 14
            allowedMaxVolume = 14
            useMusicStream = true && settingsService.canUseMusicStream()
            interval = 1
            isVibrateFirst = false
            isMuteFirst = false
        }



    fun updateConfig() {
        //        mAudioManagerWrapper.changeOutputStream(soundStream)
        //        configFactory.updateStream(mSettingsService.soundStream)
        updateStream(settingsService.soundStream)

        //        configFactory.setMaxHardwareVolumeLevel(mAudioManagerWrapper.chosenStreamrMaxHardwareVolumeLevel)
        updateMaxHardwareVolumeLevel()

        localConfigUpdate()

        config.interval = settingsService.interval

        config.isMuteFirst = settingsService.isMuteFirst
        config.muteTimes = settingsService.muteTimesCount

        config.isVibrateFirst = settingsService.isVibrateFirst
        config.vibrateTimes = settingsService.vibrateTimesCount

        config.doNotRing = settingsService.isSkipRing

        // in case of change upper volume limit
        updateMinMaxForConfig(1)
    }


    private fun localConfigUpdate() {
        minVolLimit = settingsService.minVolumeLimit
        isMinVolLimited = settingsService.isMinVolumeLimited

        isMinLimitToPreRing = isMinVolLimited && isLimitEqualsToPreRing(minVolLimit)

        maxVolLimit = settingsService.maxVolumeLimit
        isMaxVolLimited = settingsService.isMaxVolumeLimited

        isMaxLimitToPreRing = isMaxVolLimited && isLimitEqualsToPreRing(maxVolLimit)

    }

    private fun updateMinMaxForConfig(preRingLevel: Int) {

        config.startSoundLevel = when {
            isMinVolLimited && isMinLimitToPreRing -> preRingLevel
            isMaxVolLimited -> minVolLimit
            else -> 1
        }

        config.allowedMaxVolume = when{
            isMaxVolLimited && isMaxLimitToPreRing -> preRingLevel
            isMaxVolLimited -> maxVolLimit
            else -> maxHardwareVolumeLevel
        }
    }


    private fun isLimitEqualsToPreRing(resultLevel: Int): Boolean {
        return resultLevel == maxHardwareVolumeLevel + 1
    }


    private fun updateStream(soundStream: Int) {
        config.useMusicStream = soundStream == AudioManager.STREAM_MUSIC
    }

    private fun getCurrentChosenStreamVolume(): Int = audio.new_GetAudioLevel()


    fun printDebug() {
        LOG.debug("Current volume is $config")
        LOG.debug("Config is ${getCurrentChosenStreamVolume()}")
    }
}
