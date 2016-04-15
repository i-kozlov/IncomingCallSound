package com.baybaka.increasingring.statelistener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.baybaka.increasingring.RunTimeSettings;
import com.baybaka.increasingring.SettingsService;
import com.baybaka.increasingring.contoller.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MyPhoneStateListener extends PhoneStateListener {

    private final Logger LOG = LoggerFactory.getLogger(MyPhoneStateListener.class.getSimpleName());
    private State savedState = State.IDLE;

    Controller controller;
    SettingsService mSettingsService;
    RunTimeSettings mRunTimeSettings;


    @Inject
    public MyPhoneStateListener(Controller controller, SettingsService settingsService, RunTimeSettings runTimeSettings) {
        this.controller = controller;
        mSettingsService = settingsService;
        mRunTimeSettings = runTimeSettings;
    }

    private enum State {
        IDLE, RINGING, OFFHOOK;

        public static State valueOf(int callState) {
            switch (callState) {
                case TelephonyManager.CALL_STATE_IDLE:
                    return IDLE;
                case TelephonyManager.CALL_STATE_RINGING:
                    return RINGING;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    return OFFHOOK;

                default:
                    return IDLE;
            }
        }
    }

    @Override
    public void onCallStateChanged(int callState, String incomingNumber) {
        State newCallState = State.valueOf(callState);

        if (mRunTimeSettings.isLoggingEnabled()) {
            LOG.info("Received new call state = {}", newCallState);
        }

        if (!savedState.equals(newCallState)) {

            processFSM(newCallState);

            findPhone(newCallState, incomingNumber);
        }
    }

    private void findPhone(State newCallState, String incomingNumber) {
        if (State.RINGING.equals(newCallState)) {
            controller.findPhone(incomingNumber);
        }
    }

    private void processFSM(final State newCallState) {
        if (mRunTimeSettings.isLoggingEnabled()) {
            LOG.debug("Processing switch from {} to {}", savedState, newCallState);
        }

        switch (savedState) {
            case OFFHOOK: {

                if (State.RINGING.equals(newCallState)) {
                    break;
                } else if (State.IDLE.equals(newCallState)) {
                    savedState = newCallState;
                }
                break;
            }

            case RINGING: {
                if (State.OFFHOOK.equals(newCallState)) {
                    controller.stopVolumeIncrease();
                    // do no not switch state any more. doesnt work on some models
//                    savedState = newCallState;

                    break;
                } else if (State.IDLE.equals(newCallState)) {
                    savedState = newCallState;
                    controller.restoreVolumeToPreRingingLevel();
                }
                break;
            }

            case IDLE: {
                // change state for both
                savedState = newCallState;

                // if state set to ringing -> control volume
                if (State.RINGING.equals(newCallState)) {
                    controller.startVolumeIncrease();
                }
                break;
            }
        }
    }
}
