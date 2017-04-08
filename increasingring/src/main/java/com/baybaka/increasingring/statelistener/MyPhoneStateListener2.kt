package com.baybaka.increasingring.statelistener

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.baybaka.increasingring.contoller.Controller
import com.baybaka.increasingring.settings.RunTimeSettings
import org.slf4j.LoggerFactory
import javax.inject.Inject

class MyPhoneStateListener2 @Inject
constructor(private val controller: Controller,
            private val mRunTimeSettings: RunTimeSettings) : PhoneStateListener() {

    companion object {
        private val LOG = LoggerFactory.getLogger(MyPhoneStateListener2::class.java.simpleName)
    }

    private var oldState = State.IDLE

    private enum class State {
        IDLE, RINGING, OFFHOOK;

        companion object {

            fun valueOf(callState: Int): State {
                when (callState) {
                    TelephonyManager.CALL_STATE_IDLE -> return IDLE
                    TelephonyManager.CALL_STATE_RINGING -> return RINGING
                    TelephonyManager.CALL_STATE_OFFHOOK -> return OFFHOOK

                    else -> return IDLE
                }
            }
        }
    }

    override fun onCallStateChanged(callState: Int, incomingNumber: String?) {
        val newCallState = State.valueOf(callState)

        if (mRunTimeSettings.isLoggingEnabled) {
            LOG.info("Received new call state = $newCallState")
        }


        if (oldState != newCallState) {
            processFSM(newCallState, incomingNumber?: "")
        }
    }


    private fun processFSM(newCallState: State, incomingNumber: String) {

        if (mRunTimeSettings.isLoggingEnabled) {
            LOG.debug("Processing switch from {} to {}", oldState, newCallState)
        }


        when (oldState to newCallState) {

        //second line call. ignore it
            State.OFFHOOK to State.RINGING -> {
            }

        //end of talk
            State.OFFHOOK to State.IDLE -> oldState = newCallState

        //start talking
            State.RINGING to State.OFFHOOK -> {
                controller.stopVolumeIncrease()

                /* do no not switch state any more. calling restoreVolumeToPreRingingLevel
                 doesn't work on some devices*/
                // oldState = newCallState;
            }

        //no answer -> stopped ringing
        //virtually this is also called when hang up after call end
            State.RINGING to State.IDLE -> {
                oldState = newCallState
                controller.restoreVolumeToPreRingingLevel()
            }

            State.IDLE to State.OFFHOOK -> oldState = newCallState
        //new incoming call
            State.IDLE to State.RINGING -> {
                oldState = newCallState
                controller.startVolumeIncrease(incomingNumber)
            }


        }
    }
}
