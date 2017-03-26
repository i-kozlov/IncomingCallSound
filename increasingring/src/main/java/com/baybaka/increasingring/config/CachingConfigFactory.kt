package com.baybaka.increasingring.config

import android.content.Context
import android.media.AudioManager
import com.baybaka.increasingring.audio.AudioController
import com.baybaka.increasingring.audio.IAudioController
import com.baybaka.increasingring.audio.MusicImpl
import com.baybaka.increasingring.audio.RingtoneImpl
import com.baybaka.increasingring.contoller.IVolumeIncreaseThread
import com.baybaka.increasingring.contoller.RingAsMusic
import com.baybaka.increasingring.contoller.RingTone
import com.baybaka.increasingring.service.IMediaPlayerProvider
import com.baybaka.increasingring.service.MediaPlayerProvider
import com.baybaka.increasingring.settings.RunTimeSettings
import com.baybaka.increasingring.settings.SettingsService
import com.baybaka.increasingring.utils.AudioManagerWrapper
import javax.inject.Inject

class CachingConfigFactory @Inject
constructor(private val settingsService: SettingsService) {

    private val config: RingerConfig = RingerConfig()
    private var maxHardwareVolumeLevel: Int = 0
    private var oldPreRingLevel: Int = 0

    private var isMinVolLimited: Boolean = false
    private var isMinLimitToPreRing: Boolean = false
    private var minVolLimit: Int = 1

    private var isMaxVolLimited: Boolean = false
    private var isMaxLimitToPreRing: Boolean = false
    private var maxVolLimit: Int = 99


//    fun setMaxHardwareVolumeLevel(maxHardwareVolumeLevel: Int) {
//        this.maxHardwareVolumeLevel = maxHardwareVolumeLevel
//    }


    fun updateMaxHardwareVolumeLevel() {
        this.maxHardwareVolumeLevel = currentAudioController().new_GetMaxLevel()
    }



    fun getConfig(preRingLevel: Int): RingerConfig {

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

//    private fun readMuteTimes(): Int {
//        return settingsService.muteTimesCount
//    }
//
//    private fun readVibrateTimes(): Int {
//        return settingsService.vibrateTimesCount
//    }

    private fun localConfigUpdate() {
        minVolLimit = settingsService.minVolumeLimit
        isMinVolLimited = settingsService.isMinVolumeLimited

        isMinLimitToPreRing = isMinVolLimited && isLimitEqualsToPreRing(minVolLimit)

        maxVolLimit = settingsService.maxVolumeLimit
        isMaxVolLimited = settingsService.isMaxVolumeLimited

        isMaxLimitToPreRing = isMaxVolLimited && isLimitEqualsToPreRing(maxVolLimit)

    }

    private fun updateMinMaxForConfig(preRingLevel: Int) {

        var minLimitCalc = 1
        if (isMinVolLimited) {
            minLimitCalc = if (isMinLimitToPreRing) preRingLevel else minVolLimit
        }

        var maxLimitCalc = maxHardwareVolumeLevel
        if (isMaxVolLimited) {
            maxLimitCalc = if (isMaxLimitToPreRing) preRingLevel else maxVolLimit
        }

        config.startSoundLevel = minLimitCalc
        config.allowedMaxVolume = maxLimitCalc
    }


    private fun isLimitEqualsToPreRing(resultLevel: Int): Boolean {
        return resultLevel == maxHardwareVolumeLevel + 1
    }

//    private fun readIntervalConfig(): Int {
//        return settingsService.interval
//    }
//
//    private fun readMuteConfig(): Boolean {
//        return settingsService.isMuteFirst
//    }
//
//    private fun readVibrateConfig(): Boolean {
//        return settingsService.isVibrateFirst
//    }

//    @Deprecated("should be removed")
    private fun updateStream(soundStream: Int) {
        config.useMusicStream = soundStream == AudioManager.STREAM_MUSIC
    }

    fun getCurrentChosenStreamVolume(): Int =
            if (config.useMusicStream) audioControllerMusic.new_GetAudioLevel()
            else audioControllerRingTone.new_GetAudioLevel()

    //todo move all this to AudioManager
    private lateinit var playerProvider: IMediaPlayerProvider

    private lateinit var mAudioManagerWrapper: AudioManagerWrapper
    private lateinit var mRunTimeSettings: RunTimeSettings
    private lateinit var systemAudioManager: AudioManager


    fun initTemp(runTimeSettings: RunTimeSettings,
                 audioManagerWrapper: AudioManagerWrapper
    ) {
        this.playerProvider = MediaPlayerProvider(runTimeSettings.context)
        mAudioManagerWrapper = audioManagerWrapper
        mRunTimeSettings = runTimeSettings
        systemAudioManager = mRunTimeSettings.context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    val music by lazy { MusicImpl(systemAudioManager) }
    val ringtone by lazy { RingtoneImpl(systemAudioManager) }

    val audioControllerMusic by lazy { AudioController(music, mAudioManagerWrapper) }
    val audioControllerRingTone by lazy { AudioController(ringtone, mAudioManagerWrapper) }

    //todo use change modes
    fun currentAudioController(): IAudioController = if (config.useMusicStream) audioControllerMusic else audioControllerRingTone

    //todo move to config and make less ugly. Replace with strategy pattern maybe?
    fun createThread(config: RingerConfig, callerNumber: String): IVolumeIncreaseThread =
            if (config.useMusicStream)
                RingAsMusic(config, audioControllerMusic, mRunTimeSettings, callerNumber, playerProvider)
            else RingTone(config, audioControllerRingTone, mRunTimeSettings)
}
