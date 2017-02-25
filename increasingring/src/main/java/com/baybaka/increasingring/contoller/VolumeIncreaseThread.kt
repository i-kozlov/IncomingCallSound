package com.baybaka.increasingring.contoller

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import com.baybaka.increasingring.RunTimeSettings
import com.baybaka.increasingring.config.RingerConfig
import com.baybaka.increasingring.receivers.PowerButtonReceiver
import com.baybaka.increasingring.service.RingToneT
import com.baybaka.increasingring.utils.AudioManagerWrapper
import org.slf4j.LoggerFactory

abstract class VolumeIncreaseThread(val config: RingerConfig,
                                    val mAudioManagerWrapper: AudioManagerWrapper, val rts: RunTimeSettings) : Runnable {

    companion object {
        private val LOG = LoggerFactory.getLogger(VolumeIncreaseThread::class.java.simpleName)

    }

    @Volatile protected var treadStopped = false

    abstract protected fun configOutputStream()
    open fun stop() {
        treadStopped = true
    }


    override fun run() {

        fun stillPlaying(level: Int) = !treadStopped && level <= config.allowedMaxVolume

        muteAction()
        vibrateAction()

        var currentSoundLevel = config.startSoundLevel
        //todo подумать, возможно стоит делать ++ в mute и vibrate
        if (currentSoundLevel < 1)
            currentSoundLevel = 1

        if (stillPlaying(currentSoundLevel)) {
            configOutputStream()
        }

        while (stillPlaying(currentSoundLevel)) {

            LOG.info("Loop volume var = {}. Calling sound++", currentSoundLevel)

            mAudioManagerWrapper.setAudioLevelRespectingLogging(currentSoundLevel)


            currentSoundLevel++
            waitIntervalSeconds()

        }
    }

    private fun muteAction() {
        if (config.isMuteFirst) {
            mAudioManagerWrapper.muteStream()
            for (i in 1..config.muteTimes) {
                if (rts.isLoggingEnabled) {
                    LOG.info("mute {} time", i)
                }
                waitIntervalSeconds()
            }

        }
    }

    private fun vibrateAction() {
        if (config.isVibrateFirst) {
            mAudioManagerWrapper.vibrateMode()
            for (i in 1..config.vibrateTimes) {
                if (rts.isLoggingEnabled) {
                    LOG.info("vibrate {} time", i)
                }
                waitIntervalSeconds()
            }
        }

    }

    private fun waitIntervalSeconds() {
        try {
            for (i in 1..config.interval * 2) {
                if (treadStopped) break
                Thread.sleep(500)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

}

open class RingTone(config: RingerConfig, mAudioManagerWrapper: AudioManagerWrapper, rts: RunTimeSettings)
    : VolumeIncreaseThread(config, mAudioManagerWrapper, rts) {



    override fun configOutputStream() {
        mAudioManagerWrapper.normalModeStream()
    }
}

open class Music(config: RingerConfig, audioManagerWrapper: AudioManagerWrapper, rts: RunTimeSettings, val callerNumber: String, val ringtoneService: RingToneT)
    : RingTone(config, audioManagerWrapper, rts) {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mReceiver: BroadcastReceiver

    override fun stop() {
        treadStopped = true
        releasePlayer()

        rts.context.unregisterReceiver(mReceiver)
    }

    private fun releasePlayer() {
        if (mediaPlayer.isPlaying) {
            this.mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.release()

    }

    override fun configOutputStream() {
        //todo should be different method for ringtone and music
        mAudioManagerWrapper.normalModeStream()

        val player = ringtoneService.configurePlayer(callerNumber)

        player?.let {
            mediaPlayer = it
            mediaPlayer.start()

            //configure music of with power key
            val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            //                filter.addAction(Intent.ACTION_SCREEN_OFF);
            mReceiver = PowerButtonReceiver()
            rts.context.registerReceiver(mReceiver, filter)
        }

        //show notify otherwise
        if (player == null) {
            rts.errorNotification(rts.context)
        }

    }

}