package com.baybaka.increasingring.contoller

import com.baybaka.increasingring.audio.RingMode
import com.baybaka.increasingring.config.ConfigProvider
import com.baybaka.increasingring.receivers.TestPageOnCallEvenReceiver
import com.baybaka.increasingring.settings.RunTimeSettings
import com.baybaka.increasingring.settings.SettingsService
import com.baybaka.notificationlib.NotificationController
import org.slf4j.LoggerFactory
import javax.inject.Inject


class Controller @Inject
constructor(private val mSettingsService: SettingsService,
            private val mRunTimeSettings: RunTimeSettings,
//            private val mAudioManagerWrapper: IAudioController,
//            private val configFactory: CachingConfigFactory,
            private val config: ConfigProvider,
            private val mSoundRestorer: SoundRestorer,
            private val notificationController: NotificationController) {

    companion object{
        private val LOG = LoggerFactory.getLogger(Controller::class.java.simpleName)
    }

    private val phoneFinder: PhoneFinder
    private var currentThread: IVolumeIncreaseThread? = null

    private var ringWhenMute: Boolean = false


    init {
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

        val ringerMode = config.ringerMode
        someLogging()

        if (ringWhenMute || ringerMode === RingMode.RINGER_MODE_NORMAL) {
            mSoundRestorer.saveCurrentSoundLevels()

            currentThread = config.createThread(callerNumber)
            Thread(currentThread).start()

            TestPageOnCallEvenReceiver.sendBroadcastToLogReceiver(mRunTimeSettings.context)

        } else {
            LOG.info("Abort increasing volume. Phone mode is {} and ringWhenMute function set to {}", ringerMode, false)
        }
    }
    private fun someLogging(){
        if (mRunTimeSettings.isLoggingEnabled) {
            LOG.debug("Inside increaseVolume, Ringer_MODE is {}", config.ringerMode)
            config.printDebug()
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
        config.updateConfig()

        mSoundRestorer.updateConfig()
        phoneFinder.update()

//        mAudioManagerWrapper.changeStrategy(configFactory.getConfig())
        updateLocal()
    }

    private fun updateLocal() {
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

        currentThread = config.createFindPhoneThread(callerNumber)
        Thread(currentThread).start()

        mRunTimeSettings.getNotifyProvider().findPhoneCalled()
    }

}
