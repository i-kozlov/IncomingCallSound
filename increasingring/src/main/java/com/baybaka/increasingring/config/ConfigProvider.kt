package com.baybaka.increasingring.config

import com.baybaka.increasingring.audio.IAudioController
import com.baybaka.increasingring.audio.RingMode
import com.baybaka.increasingring.contoller.IVolumeIncreaseThread
import com.baybaka.increasingring.contoller.RingAsMusic
import com.baybaka.increasingring.contoller.RingTone
import com.baybaka.increasingring.service.MediaPlayerProvider
import com.baybaka.increasingring.settings.RunTimeSettings
import javax.inject.Inject

class ConfigProvider @Inject
constructor(internal val cf: CachingConfigFactory,
            val tp:ThreadProvider,
            private val audio: IAudioController) {

    fun createThread(callerNumber: String) = tp.createThread(callerNumber, cf.currentConfig())

    fun createFindPhoneThread(callerNumber: String)  = tp.createThread(callerNumber, cf.findPhoneConfig)
    fun printDebug() {
        cf.printDebug()
    }

    fun updateConfig() {
        cf.updateConfig()
        audio.changeStrategy(cf.getConfig())
    }

    val ringerMode: RingMode
        get() = audio.ringerMode
}

class ThreadProvider(val mRunTimeSettings: RunTimeSettings,
                     val audio: IAudioController){

    private val playerProvider = MediaPlayerProvider(mRunTimeSettings.context)

    fun createThread(callerNumber: String, config: RingerConfig): IVolumeIncreaseThread {

        return if (config.useMusicStream)
            RingAsMusic(config, audio, mRunTimeSettings, callerNumber, playerProvider)
        else RingTone(config, audio, mRunTimeSettings)

    }
}