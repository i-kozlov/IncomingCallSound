package com.baybaka.increasingring.contoller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
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
        var currentSoundLevel = config.startSoundLevel

//        //todo should it ++ in mute & vibrate ?
//        if (currentSoundLevel < 1)
//            currentSoundLevel = 1
//
//        //todo replace this with doNotRing check
//        if (oneMoreTurn(currentSoundLevel)) {
//            configOutputStream()
//        }
        if (config.doNotRing) {
            this.stop()
        } else {
            configOutputStream()
        }

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

open class RingTone(config: RingerConfig, audioController: IAudioController, rts: RunTimeSettings)
    : VolumeIncreaseThread(config, audioController, rts) {


    override fun configOutputStream() {
        audioController.normal()
//        mAudioManagerWrapper.normalModeStream()
    }
}

open class RingAsMusic(config: RingerConfig, audioController: IAudioController, rts: RunTimeSettings,
                       val callerNumber: String, val playerProvider: IMediaPlayerProvider)
    : RingTone(config, audioController, rts) {

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
        }


    }

    override fun configOutputStream() {
        //todo should be different method for ringtone and music
        audioController.normal()

        mediaPlayer = playerProvider.getConfiguredPlayer(callerNumber)

        mediaPlayer?.let { player ->
            val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

//todo complete or remove
// Request audio focus for playback
            val result = am.requestAudioFocus(focusChangeListener
                    ,
                    // Use the music stream.
                    AudioManager.STREAM_MUSIC,
                    // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)


            player.start()
            //configure music off with power key
            mReceiver = PowerButtonReceiver()
            //todo test music stop with screen off
            context.registerReceiver(mReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
        }

        //show notify otherwise
        if (mediaPlayer == null) {
            //todo could be replaced with AsyncTask.onUpdate
            rts.getNotifyProvider().playerInitError()
        }

    }

    private val focusChangeListener: AudioManager.OnAudioFocusChangeListener =
            AudioManager.OnAudioFocusChangeListener() {
                fun onAudioFocusChange(focusChange: Int) {
                    LOG.info("focusChangeListener is called")
//                    AudioManager am =(AudioManager) getSystemService (Context.AUDIO_SERVICE);
                    when (focusChange) {

                        (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) ->
                            // Lower the volume while ducking.
                            mediaPlayer?.setVolume(0.2f, 0.2f);

                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        }

                        (AudioManager.AUDIOFOCUS_LOSS) -> {
                        }
//                        stop();
//                        ComponentName component = new ComponentName(AudioPlayerActivity.this, MediaControlReceiver.class);
//                        am.unregisterMediaButtonEventReceiver(component);
//                        break;

                        AudioManager.AUDIOFOCUS_GAIN -> {
                            // Return the volume to normal and resume if paused.
                            mediaPlayer?.setVolume(1f, 1f);
                            mediaPlayer?.start();
//                            break;
//                            default: break;
                        }

                    }
                }
            };

    fun f(): AudioManager.OnAudioFocusChangeListener =
            AudioManager.OnAudioFocusChangeListener({ focusChange -> print(focusChange) })

}