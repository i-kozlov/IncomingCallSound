package com.baybaka.increasingring.audio

import android.media.AudioManager
import com.baybaka.increasingring.config.RingerConfig
import com.baybaka.increasingring.utils.AudioManagerWrapper
import javax.inject.Inject

interface IAudioController : AudioManagerWrapper, ModeSwitcher {
    fun new_SetAudioLevel(soundLevel: Int)
    fun new_GetAudioLevel(): Int
    fun new_GetMaxLevel(): Int
    fun mode(): ModeSwitcher
    fun changeStrategy(config: RingerConfig)
}


class AudioController @Inject
constructor(var modeSwitcher: ModeSwitcher,
                      val audioManagerWrapper: AudioManagerWrapper,
                      am: AudioManager) : IAudioController,
        ModeSwitcher by modeSwitcher, AudioManagerWrapper by audioManagerWrapper {




//    override val STREAM_TYPE: Int
//        get() = modeSwitcher.STREAM_TYPE

//    override fun normal() {
//        modeSwitcher.normal()
//    }


    private val ringtone = modeSwitcher
//    init {
//        modeSwitcher = ringtone
//    }

    private val music by lazy { MusicImpl(am) }

    override fun changeStrategy(config: RingerConfig) {
        modeSwitcher = if (config.useMusicStream) music else ringtone
    }

    override fun mode(): ModeSwitcher = modeSwitcher

    override fun new_SetAudioLevel(soundLevel: Int) = setAudioLevelRespectingLogging(soundLevel, modeSwitcher.STREAM_TYPE)
    override fun new_GetAudioLevel() = audioManagerWrapper.getStreamVolume(modeSwitcher.STREAM_TYPE)
    override fun new_GetMaxLevel() = getStreamMaxHardwareVolumeLevel(modeSwitcher.STREAM_TYPE)

}