package com.baybaka.increasingring;

import android.telephony.TelephonyManager;

import com.baybaka.increasingring.settings.SettingsService;
import com.baybaka.increasingring.statelistener.MyPhoneStateListener2;
import com.baybaka.increasingring.utils.AudioManagerWrapper;
import com.baybaka.increasingring.utils.TestConfiguration;
import com.baybaka.increasingring.utils.audio.AudioManagerFakeImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({MyPhoneStateListener2.class, LoggerFactory.class})
public class MyPhoneStateListenerTest {

    private MyPhoneStateListener2 mMyPhoneStateListener;
    private int currentSoundLevel;
    private AudioManagerWrapper mFakewrapper;
    private int interval = 1;
    private SettingsService mSharedPreferenceController;

    @Before
    public void setup() {
        Logger mockLogger = TestConfiguration.createAndConfigureMockLogger();
        mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(anyString())).thenReturn(mockLogger);

        mSharedPreferenceController = Mockito.mock(SettingsService.class);
        Mockito.when(mSharedPreferenceController.getInterval()).thenReturn(interval);

        currentSoundLevel = 1;
        mFakewrapper = new AudioManagerFakeImpl(currentSoundLevel);
//        mMyPhoneStateListener = new MyPhoneStateListener(new Controller(mSharedPreferenceController, mFakewrapper));

    }

    @Test
    public void testValueIsRestoredAfterCall() throws InterruptedException {
        //setStartFromMin();

//        mFakewrapper.setAudioLevelRespectingLogging(4);

        setNewState(TelephonyManager.CALL_STATE_RINGING);

        waitSeconds(3);
        assertThat(mFakewrapper.getCurrentChosenStreamVolume(), is(equalTo(3)));
        assertTrue(compareMinusOneIsOk(3));
        setNewState(TelephonyManager.CALL_STATE_OFFHOOK);

        assertThat(mFakewrapper.getCurrentChosenStreamVolume(), is(equalTo(4)));

        setNewState(TelephonyManager.CALL_STATE_RINGING);
        waitSeconds(2);
        assertThat(mFakewrapper.getCurrentChosenStreamVolume(), is(equalTo(4)));

    }

    private boolean compareMinusOneIsOk(int expectedLevel) {
        int currentLevel = mFakewrapper.getCurrentChosenStreamVolume();
        System.out.printf("was %s expected %s \n", currentLevel, expectedLevel);
        return expectedLevel == currentLevel || expectedLevel + 1 == currentLevel || expectedLevel - 1 == currentLevel;
    }

    private void waitSeconds(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setNewState(int state) {
        mMyPhoneStateListener.onCallStateChanged(state, "");
    }

}