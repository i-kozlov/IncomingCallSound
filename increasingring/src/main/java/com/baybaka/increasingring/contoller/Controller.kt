package com.baybaka.increasingring.contoller

import com.baybaka.increasingring.audio.RingMode
import com.baybaka.increasingring.config.CachingConfigFactory
import com.baybaka.increasingring.receivers.TestPageOnCallEvenReceiver
import com.baybaka.increasingring.settings.RunTimeSettings
import com.baybaka.increasingring.settings.SettingsService
import com.baybaka.increasingring.utils.AudioManagerWrapper
import com.baybaka.notificationlib.NotificationController
import org.slf4j.LoggerFactory
import javax.inject.Inject


class Controller @Inject
constructor(private val mSettingsService: SettingsService,
            private val mRunTimeSettings: RunTimeSettings,
            private val mAudioManagerWrapper: AudioManagerWrapper,
            private val configFactory: CachingConfigFactory,
            private val mSoundRestorer: SoundRestorer,
            private val notificationController: NotificationController) {

    companion object{
        private val LOG = LoggerFactory.getLogger(Controller::class.java.simpleName)
    }

    private val phoneFinder: PhoneFinder

    private var currentThread: IVolumeIncreaseThread? = null

    //    private boolean fastModeEnabled = false;
    private var ringWhenMute: Boolean = false


    init {

        configFactory.initTemp(mRunTimeSettings, mAudioManagerWrapper)
        this.phoneFinder = PhoneFinderImpl(mSettingsService)
        updateAllConfigs()
    }

    fun startVolumeIncrease(callerNumber: String) {

        //to make sure no old increase task left
        stopVolumeIncrease()

        checkForConfigUpdate()

        val searching = phoneFinder.lostMe(callerNumber)
        if (searching) {
            //use find phone
            findPhoneMaximizeVolume(callerNumber)
        } else {
            //use usual style
            standardFlow(callerNumber)
        }

        notificationController.startNotify()
    }

    private fun standardFlow(callerNumber: String) {

        //todo keep this 2 only?
        val ringerMode = mAudioManagerWrapper.ringerMode

        //        int preRingLevel = mAudioManagerWrapper.getCurrentChosenStreamVolume();
        val preRingLevel = configFactory.getCurrentChosenStreamVolume()


        if (mRunTimeSettings.isLoggingEnabled) {
            LOG.debug("Inside increaseVolume, Ringer_MODE is {} and volume is {}", ringerMode, preRingLevel)
            LOG.debug("Config is {} ", configFactory.getConfig())
        }

        if (ringWhenMute || ringerMode === RingMode.RINGER_MODE_NORMAL) {
            mSoundRestorer.saveCurrentSoundLevels()

            asapAction()

            val config = configFactory.getConfig(preRingLevel)
            currentThread = configFactory.createThread(config, callerNumber)
            Thread(currentThread).start()

            TestPageOnCallEvenReceiver.sendBroadcastToLogReceiver(mRunTimeSettings.context)

        } else {
            LOG.info("Abort increasing volume. Phone mode is {} and ringWhenMute function set to {}", ringerMode, false)
        }
    }


    /**
     * Fix for first call with max sound on some phones
     */
    private fun asapAction() {
        if (true/*fastModeEnabled*/) {
            mAudioManagerWrapper.setAudioLevelRespectingLogging(1)
        }
    }

    fun restoreVolumeToPreRingingLevel() {
        stopVolumeIncrease()
        mSoundRestorer.restoreVolumeToPreRingingLevel()
    }

    private fun checkForConfigUpdate() {
        if (mRunTimeSettings.isConfigChanged) {
            updateAllConfigs()
            mRunTimeSettings.configIsUpdated()
        }
    }

    fun updateAllConfigs() {
//        mAudioManagerWrapper.changeOutputStream(soundStream)
//        configFactory.updateStream(mSettingsService.soundStream)

        //todo should it do this itself?
//        configFactory.setMaxHardwareVolumeLevel(mAudioManagerWrapper.chosenStreamrMaxHardwareVolumeLevel)

        //todo same
        configFactory.updateConfig()

        mSoundRestorer.updateConfig()
        phoneFinder.update()

        updateLocal()
    }

    private fun updateLocal() {
        //        fastModeEnabled = settings.getAsapState();
        //        fastModeEnabled = true;
        ringWhenMute = mSettingsService.ringWhenMute()

    }


    fun stopVolumeIncrease() {
        currentThread?.let {
            it.stop()
            currentThread = null
        }

        notificationController.stopNotify()
    }


    private fun findPhoneMaximizeVolume(callerNumber: String) {
        LOG.info("Calling find my phone function for number $callerNumber")
        mSoundRestorer.saveCurrentSoundLevels()

        val config = configFactory.findPhoneConfig
        currentThread = configFactory.createThread(config, callerNumber)
        Thread(currentThread).start()


        //        mAudioManagerWrapper.maxVolDisableMuteVibrate();
        //        updateAllConfigs();
        mRunTimeSettings.getNotifyProvider().findPhoneCalled()
    }

}
