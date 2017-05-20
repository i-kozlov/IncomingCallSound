package com.baybaka.increasingring.config

import android.media.AudioManager
import com.baybaka.increasingring.audio.IAudioController
import com.baybaka.increasingring.contoller.IVolumeIncreaseThread
import com.baybaka.increasingring.contoller.RingAsMusic
import com.baybaka.increasingring.contoller.RingTone
import com.baybaka.increasingring.service.IMediaPlayerProvider
import com.baybaka.increasingring.service.MediaPlayerProvider
import com.baybaka.increasingring.settings.RunTimeSettings
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

//    fun setMaxHardwareVolumeLevel(maxHardwareVolumeLevel: Int) {
//        this.maxHardwareVolumeLevel = maxHardwareVolumeLevel
//    }


    fun updateMaxHardwareVolumeLevel() {
        this.maxHardwareVolumeLevel = audio.new_GetMaxLevel()
    }



    private fun currentConfig(): RingerConfig {
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

    //todo caching ignores when music permissions granted
    val findPhoneConfig: RingerConfig by lazy {
        with(RingerConfig()) {
            startSoundLevel = 14
            allowedMaxVolume = 14
            useMusicStream = true && settingsService.canUseMusicStream()
            interval = 1
            isVibrateFirst = false
            isMuteFirst = false
            return@with this
        }
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

    //todo move all this to AudioManager
    private lateinit var playerProvider: IMediaPlayerProvider

    private lateinit var mRunTimeSettings: RunTimeSettings

//    private lateinit var mAudioManagerWrapper: AudioManagerWrapper
//    private lateinit var systemAudioManager: AudioManager


    fun initTemp(runTimeSettings: RunTimeSettings
    ) {
        this.playerProvider = MediaPlayerProvider(runTimeSettings.context)
        mRunTimeSettings = runTimeSettings
    }

    fun createThread(callerNumber: String, config: RingerConfig = currentConfig()): IVolumeIncreaseThread {

        return if (config.useMusicStream)
            RingAsMusic(config, audio, mRunTimeSettings, callerNumber, playerProvider)
        else RingTone(config, audio, mRunTimeSettings)

    }

    fun createFindPhoneThread(callerNumber: String): IVolumeIncreaseThread {
        return createThread(callerNumber, findPhoneConfig)
    }


    fun printDebug() {
        LOG.debug("Current volume is $config")
        LOG.debug("Config is ${getCurrentChosenStreamVolume()}")
    }
}
