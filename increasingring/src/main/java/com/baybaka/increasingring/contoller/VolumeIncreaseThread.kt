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

        fun notMaxAndRinging(level: Int) = !treadStopped && level <= config.allowedMaxVolume

        muteAction()
        vibrateAction()

        var currentSoundLevel = config.startSoundLevel

        //todo should it ++ in mute & vibrate
        if (currentSoundLevel < 1)
            currentSoundLevel = 1

        if (notMaxAndRinging(currentSoundLevel)) {
            configOutputStream()
        }

        while (notMaxAndRinging(currentSoundLevel)) {

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

    private var mediaPlayer: MediaPlayer? = null
    private var mReceiver: BroadcastReceiver? = null

    override fun stop() {
        treadStopped = true
        releasePlayer()
        mReceiver?.let { rts.context.unregisterReceiver(it) }
    }

    private fun releasePlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.reset()
            it.release()
        }


    }

    override fun configOutputStream() {
        //todo should be different method for ringtone and music
        mAudioManagerWrapper.normalModeStream()

        val player = ringtoneService.configurePlayer(callerNumber)

        player?.let {
            mediaPlayer = it
            mediaPlayer?.start()

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