package com.baybaka.increasingring.audio

import com.baybaka.increasingring.utils.AudioManagerWrapper

interface IAudioController : AudioManagerWrapper, ModeSwitcher {
    fun new_SetAudioLevel(soundLevel: Int)
    fun new_GetAudioLevel(): Int
    fun new_GetMaxLevel(): Int
    fun mode(): ModeSwitcher
//    fun changeStrategy(config: RingerConfig)
}

class AudioController(var modeSwitcher: ModeSwitcher,
                      val audioManagerWrapper: AudioManagerWrapper) : IAudioController,
        ModeSwitcher by modeSwitcher, AudioManagerWrapper by audioManagerWrapper {


//    val music by lazy { MusicImpl(systemAudioManager) }
//    val ringtone by lazy { RingtoneImpl(systemAudioManager) }
//
//    override fun changeStrategy(config: RingerConfig) {
//        modeSwitcher = if (config.useMusicStream) music else ringtone
//    }

    override fun mode(): ModeSwitcher = modeSwitcher

    override fun new_SetAudioLevel(soundLevel: Int) = setAudioLevelRespectingLogging(soundLevel, modeSwitcher.STREAM_TYPE)
    override fun new_GetAudioLevel() = getStreamVolume(modeSwitcher.STREAM_TYPE)
    override fun new_GetMaxLevel() = getStreamMaxHardwareVolumeLevel(modeSwitcher.STREAM_TYPE)

}