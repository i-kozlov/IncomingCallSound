package com.baybaka.increasingring.contoller

import com.baybaka.increasingring.settings.SettingsService
import java.util.*
import java.util.concurrent.TimeUnit

interface PhoneFinder {
    fun lostMe(incomingNumber: String): Boolean
    fun update()
}

class PhoneFinderImpl(private val settingsService: SettingsService) : PhoneFinder {

    private var lastNumber = ""
    private var numCount: Int = 0

    private var numMaxCount: Int = 2
    private var enabled: Boolean = false

    private var lastCallTimeForTHISNumber = Date().time

    private val WITHIN_MINUTES = 3

    override fun lostMe(incomingNumber: String): Boolean {
        if (!enabled) return false

        var lost = false
        val newCallTime = Date().time

        val minutes = TimeUnit.MILLISECONDS.toMinutes(newCallTime - lastCallTimeForTHISNumber)
        val timeIsOk = minutes <= WITHIN_MINUTES

        if (lastNumber == incomingNumber && timeIsOk) {

            numCount++
            if (numCount >= numMaxCount) {
                lost = true
//                LOG.info("Calling find my phone function for number {}", incomingNumber)
//                findPhoneMaximizeVolume(incomingNumber)
            }

        } else {
            numCount = 1
            lastNumber = incomingNumber
        }
        lastCallTimeForTHISNumber = newCallTime

        return lost
    }

    fun enable() = { enabled = true }
    fun disable() = { enabled = false }
    fun updateCount(num: Int) = { numCount = num }

    override fun update() {
        numMaxCount = settingsService.findPhoneCount
        enabled = settingsService.isFindPhoneEnabled
        if (!enabled) {
            numCount = 0
        }
    }

    //keep that in controller
//    private fun findPhoneMaximizeVolume(callerNumber: String) {
//        stopVolumeIncrease()
//        mAudioManagerWrapper.changeOutputStream(AudioManager.STREAM_MUSIC)
//        val config = configFactory.findPhoneConfig
//        currentThread = createThread(config, callerNumber) //new VolumeIncreaseThread(config, mAudioManagerWrapper, mRunTimeSettings, callerNumber);
//        Thread(currentThread).start()
//
//        mRunTimeSettings.configurationChanged() // to reset stream on next call
//
//        //        mAudioManagerWrapper.maxVolDisableMuteVibrate();
//        //        updateAllConfigs();
//        mRunTimeSettings.findPhoneNotification()
//    }
}