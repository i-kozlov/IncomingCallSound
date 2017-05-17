package com.baybaka.increasingring.contoller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import com.baybaka.increasingring.audio.IAudioController
import com.baybaka.increasingring.config.RingerConfig
import com.baybaka.increasingring.receivers.PowerButtonReceiver
import com.baybaka.increasingring.service.IMediaPlayerProvider
import com.baybaka.increasingring.settings.RunTimeSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory


interface IVolumeIncreaseThread : Runnable {
    fun stop()
}

abstract class VolumeIncreaseThread(val config: RingerConfig,
                                    val audioController: IAudioController,
                                    val rts: RunTimeSettings) : IVolumeIncreaseThread {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(VolumeIncreaseThread::class.java.simpleName)

    }

    //todo pass as boolean than we need context only
    val isLoggingEnabled = rts.isLoggingEnabled
    val context: Context = rts.context
    @Volatile protected var treadStopped = false

    abstract protected fun configOutputStream()
    override fun stop() {
        treadStopped = true
    }


    override fun run() {

        fun oneMoreTurn(level: Int) = !treadStopped && level <= config.allowedMaxVolume

        if (isLoggingEnabled) {
            LOG.info("mode: " + audioController.mode().toString())
            LOG.info("config: " + config.toString())
        }

        muteAction()
        vibrateAction()

        if (config.doNotRing) {
            this.stop()
            LOG.info("Config doNotRing set to true. Stop before ringing")
        } else {
            configOutputStream()
        }

        var currentSoundLevel = config.startSoundLevel
        while (oneMoreTurn(currentSoundLevel)) {

            audioController.new_SetAudioLevel(currentSoundLevel)

            LOG.info("Loop volume currentSoundLevel = $currentSoundLevel. Calling sound++")

            currentSoundLevel++
            waitIntervalSeconds()

        }
    }

    private fun muteAction() {
        if (config.isMuteFirst) {
            audioController.silent()
            for (i in 1..config.muteTimes) {
                if (isLoggingEnabled) {
                    LOG.info("mute {} time", i)
                }
                waitIntervalSeconds()
            }

        }
    }

    private fun vibrateAction() {
        if (config.isVibrateFirst) {
            audioController.vibrate()
            for (i in 1..config.vibrateTimes) {
                if (isLoggingEnabled) {
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

class RingTone(config: RingerConfig, audioController: IAudioController, rts: RunTimeSettings)
    : VolumeIncreaseThread(config, audioController, rts) {


    override fun configOutputStream() {
        audioController.normal()
    }
}

class RingAsMusic(config: RingerConfig, audioController: IAudioController, rts: RunTimeSettings,
                       val callerNumber: String, val playerProvider: IMediaPlayerProvider)
    : VolumeIncreaseThread(config, audioController, rts) {

    private var mediaPlayer: MediaPlayer? = null
    private var mReceiver: BroadcastReceiver? = null

    override fun stop() {
        treadStopped = true
        releasePlayer()
        mReceiver?.let { context.unregisterReceiver(it) }
    }

    private fun releasePlayer() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.reset()
            it.release()
//            am().abandonAudioFocus(focusChangeListener)
        }


    }

//    private fun am(): AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun configOutputStream() {
        audioController.normal()

        mediaPlayer = playerProvider.getConfiguredPlayer(callerNumber)

        mediaPlayer?.let { player ->


            player.start()
            //configure music off with power key
            mReceiver = PowerButtonReceiver()
            context.registerReceiver(mReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
        }

        //show notify otherwise
        if (mediaPlayer == null) {
            rts.getNotifyProvider().playerInitError()
        }

    }


}