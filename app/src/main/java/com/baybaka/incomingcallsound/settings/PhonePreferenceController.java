package com.baybaka.incomingcallsound.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import com.baybaka.incomingcallsound.BuildConfig;
import com.baybaka.incomingcallsound.MyApp;

import javax.inject.Inject;

public class PhonePreferenceController implements AllSettings {

    protected static String prefix = "";
    protected static final String IS_SERVICE_ENABLED = prefix + "service enabled";

    protected static final String CHANGE_SOUND_INTERVAL = prefix + "sound_interval";

    protected static final String USER_MAX_VOLUME_LIMIT = prefix + "max_limit_val";
    protected static final String IS_MAX_VOLUME_LIMITED = prefix + "max_limit_enabled";

    protected static final String IS_MIN_VOLUME_LIMITED = prefix + "min_limit_enabled";
    protected static final String USER_MIN_VOLUME_LIMIT = prefix + "min_limit_val";

    protected static final String PAUSE_WA_ENABLED = prefix + "pause_wa";
    protected static final String PAUSE_WA_TIME_TO_WAIT = prefix + "pause_wa_time";

    protected static final String RESTORE_TO_USER_LVL_ENABLED = prefix + "user set level restore enabled";
    protected static final String USER_SET_SOUND_LVL = prefix + "user set level  val";

    protected static final String RING_WHEN_MUTE = prefix + "ring_when_mute";

    protected static final String RUN_FOREGROUND = prefix + "RUN_FOREGROUND";
    protected static final String FOREGROUND_NOTIFICATION_WITH_MIN_PRIORITY = prefix + "FOREGROUND_MIN_PRIORITY";

    protected static final String SOUND_STREAM = prefix + "sound_stream_number";

    protected static final String VOL_DONW_ASAP = prefix + "first_vol_down_asap";

    protected static final String LOGGING_ENABLED = prefix + "logging_enabled";

    protected static final String NOTIFICATION_USE_LED = prefix + "notification_use_led";
    protected static final String USE_FLASH_LIGHT = prefix + "USE_FLASH_LIGHT";

    protected static final String VIBRATE_TIMES = prefix + "vibrate_times_count";
    protected static final String VIBRATE_BEFORE_RING = prefix + "vibrate_before_ring";

    protected static final String MUTE_TIMES = prefix + "mute_times_count";
    protected static final String MUTE_BEFORE_RING = prefix + "mute_before_ring";

    protected static final String FIND_PHONE_FUNCTION_STATUS = prefix + "find_phone_function_status";
    protected static final String FIND_PHONE_TIMES_BEFORE_MAXIMISE = prefix + "times_before_maximise";


    protected final SharedPreferences mPreferences;

    @Inject
    public PhonePreferenceController(Context appContext) {
        mPreferences = appContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    protected void applyAndUpdateConfig(SharedPreferences.Editor editor) {
//        System.out.println("config update");
//        editor.apply();
        editor.commit();
        MyApp.get().getRunTimeSettings().configurationChanged();
        MyApp.get().getListenerComponent().controller().updateAllConfigs();

    }


    @Override
    public int getInterval() {
        int DEFAULT_INTERVAL_VALUE = 5;
        return mPreferences.getInt(CHANGE_SOUND_INTERVAL, DEFAULT_INTERVAL_VALUE);
    }


    @Override
    public boolean isServiceEnabledInConfig() {
        return mPreferences.getBoolean(IS_SERVICE_ENABLED, true);
    }

    @Override
    public void changeServiceEnabledSettings(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(IS_SERVICE_ENABLED, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public void setNewIntervalValue(int value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CHANGE_SOUND_INTERVAL, value);
        applyAndUpdateConfig(editor);

    }

    // max volume
    @Override
    public boolean isMaxVolumeLimited() {
        return mPreferences.getBoolean(IS_MAX_VOLUME_LIMITED, false);
    }

    @Override
    public void toggleMaxVolumeLimit(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(IS_MAX_VOLUME_LIMITED, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public int getMaxVolumeLimit() {
        return mPreferences.getInt(USER_MAX_VOLUME_LIMIT, 5);
    }

    @Override
    public void setMaxVolumeLimit(int maxLevel) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_MAX_VOLUME_LIMIT, maxLevel);
        applyAndUpdateConfig(editor);
    }

    //////////////////////min volume
    @Override
    public boolean isMinVolumeLimited() {
        return mPreferences.getBoolean(IS_MIN_VOLUME_LIMITED, false);
    }

    @Override
    public void enableMinVolumeLimit(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(IS_MIN_VOLUME_LIMITED, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public int getMinVolumeLimit() {
        return mPreferences.getInt(USER_MIN_VOLUME_LIMIT, 1);
    }

    @Override
    public void setMinVolumeLimit(int minLevel) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_MIN_VOLUME_LIMIT, minLevel);
        applyAndUpdateConfig(editor);
    }

    /////////pause workaroud

    @Override
    public boolean isPauseWAenabled() {
        return mPreferences.getBoolean(PAUSE_WA_ENABLED, true);
    }

    @Override
    public void togglePauseWAState(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(PAUSE_WA_ENABLED, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public int getPauseWAtimeValue() {
        return mPreferences.getInt(PAUSE_WA_TIME_TO_WAIT, 1);
    }

    @Override
    public void setPauseWAtimeValue(int pauseSec) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PAUSE_WA_TIME_TO_WAIT, pauseSec);
        applyAndUpdateConfig(editor);
    }

    ///////// restore sound

    @Override
    public boolean isRestoreVolToUserSetLevelEnabled() {
        return mPreferences.getBoolean(RESTORE_TO_USER_LVL_ENABLED, false);
    }

    @Override
    public void toggleRestoreVolToUserSetLevel(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(RESTORE_TO_USER_LVL_ENABLED, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public int getUserSetLvl() {
        return mPreferences.getInt(USER_SET_SOUND_LVL, 1);
    }

    @Override
    public void setUserSetLvl(int pauseSec) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_SET_SOUND_LVL, pauseSec);
        applyAndUpdateConfig(editor);
    }

    ////////////
    @Override
    public boolean ringWhenMute() {
        return mPreferences.getBoolean(RING_WHEN_MUTE, false);
    }

    @Override
    public void toggleIgnoreSilenceVibrate(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(RING_WHEN_MUTE, newValue);
        applyAndUpdateConfig(editor);
    }

    ////////////
    @Override
    public boolean isVibrateFirst() {
        return mPreferences.getBoolean(VIBRATE_BEFORE_RING, false);
    }

    @Override
    public void setVibrateFirst(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(VIBRATE_BEFORE_RING, newValue);
        applyAndUpdateConfig(editor);
    }

    ////////////
    @Override
    public boolean startInForeground() {
        return mPreferences.getBoolean(RUN_FOREGROUND, false);
    }

    @Override
    public void setRunForeground(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(RUN_FOREGROUND, newValue);
        applyAndUpdateConfig(editor);
    }

    ////////////
    @Override
    public boolean isLoggingEnabled() {
        return mPreferences.getBoolean(LOGGING_ENABLED, true);
    }

    @Override
    public void setLoggingState(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(LOGGING_ENABLED, newValue);
        editor.apply();
        MyApp.get().getRunTimeSettings().setGlobalLoggingState(newValue);
    }
    ////////////

    @Override
    public boolean isUsingFlash() {
        return mPreferences.getBoolean(USE_FLASH_LIGHT, false);
    }

    @Override
    public void setUsingFlash(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(USE_FLASH_LIGHT, newValue);
        editor.apply();
        MyApp.get().getListenerComponent().notification().updateConfig();
    }
    ////////////

    @Override
    public int getSoundStream() {
        return mPreferences.getInt(SOUND_STREAM, AudioManager.STREAM_RING);
    }

    @Override
    public void setSoundStream(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        int stream;
        if (newValue) {
            stream = AudioManager.STREAM_RING;
        } else
            stream = AudioManager.STREAM_MUSIC;

        editor.putInt(SOUND_STREAM, stream);
        applyAndUpdateConfig(editor);
    }

    @Override
    public void setAsapState(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(VOL_DONW_ASAP, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public boolean getAsapState() {
        return mPreferences.getBoolean(VOL_DONW_ASAP, false);
    }


    @Override
    public boolean isUseLed() {
        return mPreferences.getBoolean(NOTIFICATION_USE_LED, false);
    }

    @Override
    public void setUseLed(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(NOTIFICATION_USE_LED, newValue);
        editor.apply();
        MyApp.get().getListenerComponent().notification().updateConfig();

    }

    @Override
    public int getVibrateTimesCount() {
        return mPreferences.getInt(VIBRATE_TIMES, 1);
    }

    @Override
    public void setVibrateTimesCount(int newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(VIBRATE_TIMES, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public boolean isMuteFirst() {
        return mPreferences.getBoolean(MUTE_BEFORE_RING, false);
    }

    @Override
    public void setMuteFirst(boolean newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(MUTE_BEFORE_RING, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public int getMuteTimesCount() {
        return mPreferences.getInt(MUTE_TIMES, 1);
    }

    @Override
    public void setMuteTimesCount(int newValue) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(MUTE_TIMES, newValue);
        applyAndUpdateConfig(editor);
    }

    @Override
    public void resetConfig() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public String all() {
        return mPreferences.getAll().toString();
    }

    @Override
    public boolean isFindPhoneEnabled() {
        return mPreferences.getBoolean(FIND_PHONE_FUNCTION_STATUS, false);
    }

    @Override
    public void setFindPhoneEnabled(boolean checked) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(FIND_PHONE_FUNCTION_STATUS, checked);
        applyAndUpdateConfig(editor);
    }

    @Override
    public void setFindPhoneCount(int progress) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(FIND_PHONE_TIMES_BEFORE_MAXIMISE, progress);
        applyAndUpdateConfig(editor);
    }

    @Override
    public int getFindPhoneCount() {
        return mPreferences.getInt(FIND_PHONE_TIMES_BEFORE_MAXIMISE, 4);
    }

    @Override
    public boolean showNotificationWithMinPriority() {
        return mPreferences.getBoolean(FOREGROUND_NOTIFICATION_WITH_MIN_PRIORITY, false);
    }

    @Override
    public void setMinNotificationPriory(boolean checked) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(FOREGROUND_NOTIFICATION_WITH_MIN_PRIORITY, checked);
        editor.apply();
    }

    @Override
    public boolean isProVersion() {
        return "pro".equals(BuildConfig.FLAVOR);
    }
}
