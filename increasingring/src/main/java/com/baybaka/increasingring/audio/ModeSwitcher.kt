package com.baybaka.increasingring.audio

import android.annotation.SuppressLint
import android.media.AudioManager
import com.baybaka.increasingring.utils.PermissionChecker
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface ModeSwitcher {
    companion object{
        val LOG: Logger = LoggerFactory.getLogger(ModeSwitcher::class.java.simpleName)
        val NO_FLAGS = 0
    }
    val STREAM_TYPE : Int

    fun silent() {


        if (PermissionChecker.Version.eqOrGreaterAndroid6()) {
            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, NO_FLAGS)
        } else {
            systemAudioManager.setStreamMute(AudioManager.STREAM_RING, true)
        }

        systemAudioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        LOG.debug("mute on. ringer_mode is : {}. must be 0", systemAudioManager.ringerMode)
    }

    fun vibrate() {
        setRingerMode(AudioManager.RINGER_MODE_VIBRATE)
        LOG.debug("vibrate on. ringer_mode is : {}. must be 1", systemAudioManager.ringerMode)
    }

    fun normal()

    val systemAudioManager: AudioManager

    fun setRingVolume(level: Int) {
        systemAudioManager.setStreamVolume(AudioManager.STREAM_RING, level, NO_FLAGS)
    }

    fun setRingerMode(mode: Int) {
        systemAudioManager.ringerMode = mode
    }

}

class RingtoneImpl(override val systemAudioManager: AudioManager) : ModeSwitcher {
    override val STREAM_TYPE: Int
        get() = AudioManager.STREAM_RING

    override fun toString(): String = "STREAM_RING"

    override fun vibrate() {
        if (PermissionChecker.Version.eqOrGreaterAndroid6()) {
            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_VIBRATE)
            //                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, NO_FLAGS);
        } else {
            systemAudioManager.setStreamMute(AudioManager.STREAM_RING, false)


            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_VIBRATE)
            //                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, NO_FLAGS);
        }

        super.vibrate()
    }

    @SuppressLint("InlinedApi")
    override fun normal() {

        when{
            PermissionChecker.Version.eqOrGreaterAndroid6() ->{
                //should be called first!
                setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                //optional?
                systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0)
            }
            else -> {

                setRingerMode(AudioManager.RINGER_MODE_NORMAL)

                //optional?
//                systemAudioManager.setStreamMute(AudioManager.STREAM_RING, false)
//                adjustRingerUp()


            }
        }

        setRingVolume(1)

//        setRingerMode(AudioManager.RINGER_MODE_NORMAL)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0)
//        } else {
//            systemAudioManager.setStreamMute(AudioManager.STREAM_RING, false)
//            setRingerMode(AudioManager.RINGER_MODE_NORMAL)
//            adjustRingerUp()
//            //                systemAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//        }
//
//        setRingVolume(1)
//        setRingerMode(AudioManager.RINGER_MODE_NORMAL)
    }

    private fun adjustRingerUp() {
        systemAudioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_ALLOW_RINGER_MODES)
    }

}

class MusicImpl(override val systemAudioManager: AudioManager) : ModeSwitcher {

    override val STREAM_TYPE: Int
        get() = AudioManager.STREAM_MUSIC

    override fun toString(): String = "STREAM_MUSIC"
    override fun vibrate() {

        setRingVolume(0)

        // check do we still need it  ?
//        systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)

        super.vibrate()
    }

    override fun normal() {
        setRingVolume(0)

//        systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        systemAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0)
    }
}
