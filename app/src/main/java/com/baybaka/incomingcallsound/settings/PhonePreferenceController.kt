package com.baybaka.incomingcallsound.settings

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager

import com.baybaka.incomingcallsound.BuildConfig
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.increasingring.settings.SettingsService

import javax.inject.Inject

class PhonePreferenceController @Inject
constructor(val appContext: Context) : AllSettings {
    override fun canUseMusicStream(): Boolean {
        TODO("should not be called. Can be replaced with exception")
    }


    override fun setSimpleMode(checked: Boolean) {
        editor.putBoolean(USE_SIMPLE_MODE, checked).applyAndUpdateConfig()
    }

    override fun useSimpleMode(): Boolean = mPreferences.getBoolean(USE_SIMPLE_MODE, true)

    protected val mPreferences: SharedPreferences = appContext.getSharedPreferences(SettingsService.FILE_NAME, Context.MODE_PRIVATE)


    protected fun SharedPreferences.Editor.applyAndUpdateConfig() {
//        System.out.println("config update");
//        this.apply();
        this.commit()
        (appContext as MyApp).runTimeSettings.configurationChanged()
        appContext.listenerComponent.controller().updateAllConfigs()

    }

    val editor: SharedPreferences.Editor
        get() = mPreferences.edit()

    override fun isSkipRing(): Boolean = mPreferences.getBoolean(SKIP_RING, false)

    override fun setSkipRing(checked: Boolean) {
        editor.putBoolean(SKIP_RING, checked).applyAndUpdateConfig()
    }

    override fun getInterval(): Int {
        return mPreferences.getInt(CHANGE_SOUND_INTERVAL, DEFAULT_INTERVAL_VALUE)
    }


    override fun isServiceEnabledInConfig(): Boolean {
        return mPreferences.getBoolean(IS_SERVICE_ENABLED, true)
    }

    override fun changeServiceEnabledSettings(newValue: Boolean) {

        editor.putBoolean(IS_SERVICE_ENABLED, newValue)
                .applyAndUpdateConfig()
    }

    override fun setNewIntervalValue(value: Int) {

        editor.putInt(CHANGE_SOUND_INTERVAL, value)
                .applyAndUpdateConfig()

    }

    // max volume
    override fun isMaxVolumeLimited(): Boolean {
        return mPreferences.getBoolean(IS_MAX_VOLUME_LIMITED, false)
    }

    override fun toggleMaxVolumeLimit(newValue: Boolean) {

        editor.putBoolean(IS_MAX_VOLUME_LIMITED, newValue)
                .applyAndUpdateConfig()
    }

    override fun getMaxVolumeLimit(): Int {
        return mPreferences.getInt(USER_MAX_VOLUME_LIMIT, 5)
    }

    override fun setMaxVolumeLimit(maxLevel: Int) {

        editor.putInt(USER_MAX_VOLUME_LIMIT, maxLevel)
                .applyAndUpdateConfig()
    }

    //////////////////////min volume
    override fun isMinVolumeLimited(): Boolean {
        return mPreferences.getBoolean(IS_MIN_VOLUME_LIMITED, false)
    }

    override fun enableMinVolumeLimit(newValue: Boolean) {

        editor.putBoolean(IS_MIN_VOLUME_LIMITED, newValue)
                .applyAndUpdateConfig()
    }

    override fun getMinVolumeLimit(): Int {
        return mPreferences.getInt(USER_MIN_VOLUME_LIMIT, 1)
    }

    override fun setMinVolumeLimit(minLevel: Int) {

        editor.putInt(USER_MIN_VOLUME_LIMIT, minLevel)
                .applyAndUpdateConfig()
    }

    /////////pause workaround

    override fun isPauseWAenabled(): Boolean {
        return mPreferences.getBoolean(PAUSE_WA_ENABLED, true)
    }

    override fun togglePauseWAState(state: Boolean) {

        editor.putBoolean(PAUSE_WA_ENABLED, state)
                .applyAndUpdateConfig()
    }

    override fun getPauseWAtimeValue(): Int {
        return mPreferences.getInt(PAUSE_WA_TIME_TO_WAIT, 1)
    }

    override fun setPauseWAtimeValue(pauseSec: Int) {

        editor.putInt(PAUSE_WA_TIME_TO_WAIT, pauseSec)
                .applyAndUpdateConfig()
    }

    ///////// restore sound

    override fun isRestoreVolToUserSetLevelEnabled(): Boolean {
        return mPreferences.getBoolean(RESTORE_TO_USER_LVL_ENABLED, false)
    }

    override fun toggleRestoreVolToUserSetLevel(state: Boolean) {

        editor.putBoolean(RESTORE_TO_USER_LVL_ENABLED, state)
                .applyAndUpdateConfig()
    }

    override fun getUserSetLvl(): Int {
        return mPreferences.getInt(USER_SET_SOUND_LVL, 1)
    }

    override fun setUserSetLvl(pauseSec: Int) {

        editor.putInt(USER_SET_SOUND_LVL, pauseSec)
                .applyAndUpdateConfig()
    }

    ////////////
    override fun ringWhenMute(): Boolean {
        return mPreferences.getBoolean(RING_WHEN_MUTE, false)
    }

    override fun toggleIgnoreSilenceVibrate(state: Boolean) {

        editor.putBoolean(RING_WHEN_MUTE, state)
                .applyAndUpdateConfig()
    }

    ////////////
    override fun isVibrateFirst(): Boolean {
        return mPreferences.getBoolean(VIBRATE_BEFORE_RING, false)
    }

    override fun setVibrateFirst(newValue: Boolean) {

        editor.putBoolean(VIBRATE_BEFORE_RING, newValue)
                .applyAndUpdateConfig()
    }

    ////////////
    override fun startInForeground(): Boolean {
        return mPreferences.getBoolean(RUN_FOREGROUND, false)
    }

    override fun setRunForeground(newValue: Boolean) {

        editor.putBoolean(RUN_FOREGROUND, newValue)
                .applyAndUpdateConfig()
    }

    ////////////
    override fun isLoggingEnabled(): Boolean {
        return mPreferences.getBoolean(LOGGING_ENABLED, true)
    }

    override fun setLoggingState(newValue: Boolean) {

        editor.putBoolean(LOGGING_ENABLED, newValue)
        editor.apply()
        MyApp.get().runTimeSettings.setGlobalLoggingState(newValue)
    }
    ////////////

    override fun isUsingFlash(): Boolean {
        return mPreferences.getBoolean(USE_FLASH_LIGHT, false)
    }

    override fun setUsingFlash(newValue: Boolean) {

        editor.putBoolean(USE_FLASH_LIGHT, newValue)
        editor.apply()
        MyApp.get().listenerComponent.notification().updateConfig()
    }
    ////////////

    override fun getSoundStream(): Int {
        val streamName = mPreferences.getString(SOUND_STREAM_KEY, "STREAM_RING")

        return if (streamName == "STREAM_MUSIC") AudioManager.STREAM_MUSIC
        else AudioManager.STREAM_RING
    }

    override fun setSoundStream(STREAM_MUSIC: Boolean) {

        val stream = if (STREAM_MUSIC) "STREAM_MUSIC" else "STREAM_RING"

        editor.putString(SOUND_STREAM_KEY, stream).applyAndUpdateConfig()
    }

    override fun setAsapState(newValue: Boolean) {

        editor.putBoolean(VOL_DONW_ASAP, newValue)
                .applyAndUpdateConfig()
    }

    override fun getAsapState(): Boolean {
        return mPreferences.getBoolean(VOL_DONW_ASAP, false)
    }


    override fun isUseLed(): Boolean {
        return mPreferences.getBoolean(NOTIFICATION_USE_LED, false)
    }

    override fun setUseLed(state: Boolean) {

        editor.putBoolean(NOTIFICATION_USE_LED, state)
        editor.apply()
        MyApp.get().listenerComponent.notification().updateConfig()

    }

    override fun getVibrateTimesCount(): Int {
        return mPreferences.getInt(VIBRATE_TIMES, 1)
    }

    override fun setVibrateTimesCount(count: Int) {

        editor.putInt(VIBRATE_TIMES, count).applyAndUpdateConfig()
    }

    override fun isMuteFirst(): Boolean {
        return mPreferences.getBoolean(MUTE_BEFORE_RING, false)
    }

    override fun setMuteFirst(newValue: Boolean) {

        editor.putBoolean(MUTE_BEFORE_RING, newValue)
                .applyAndUpdateConfig()
    }

    override fun getMuteTimesCount(): Int {
        return mPreferences.getInt(MUTE_TIMES, 1)
    }

    override fun setMuteTimesCount(count: Int) {

        editor.putInt(MUTE_TIMES, count)
                .applyAndUpdateConfig()
    }

    override fun resetConfig() {
        editor.clear().commit()
        editor.applyAndUpdateConfig()
    }

    override fun smartResetConfig() {
        val keepInMemory = startInForeground()

        resetConfig()
        setRunForeground(keepInMemory)
        editor.applyAndUpdateConfig()
    }

    override fun all(): String {
        return mPreferences.all.toString()
    }

    override fun isFindPhoneEnabled(): Boolean {
        return mPreferences.getBoolean(FIND_PHONE_FUNCTION_STATUS, false)
    }

    override fun setFindPhoneEnabled(checked: Boolean) {

        editor.putBoolean(FIND_PHONE_FUNCTION_STATUS, checked)
                .applyAndUpdateConfig()
    }

    override fun setFindPhoneCount(times: Int) {

        editor.putInt(FIND_PHONE_TIMES_BEFORE_MAXIMISE, times)
                .applyAndUpdateConfig()
    }

    override fun getFindPhoneCount(): Int {
        return mPreferences.getInt(FIND_PHONE_TIMES_BEFORE_MAXIMISE, 4)
    }

    override fun showNotificationWithMinPriority(): Boolean {
        return mPreferences.getBoolean(FOREGROUND_NOTIFICATION_WITH_MIN_PRIORITY, false)
    }

    override fun setMinNotificationPriory(checked: Boolean) {

        editor.putBoolean(FOREGROUND_NOTIFICATION_WITH_MIN_PRIORITY, checked)
        editor.apply()
    }

    override fun isProVersion(): Boolean {
        return "pro" == BuildConfig.FLAVOR
    }

    companion object {

        protected var prefix = ""
        protected val IS_SERVICE_ENABLED = prefix + "service enabled"

        protected val CHANGE_SOUND_INTERVAL = prefix + "sound_interval"

        protected val USER_MAX_VOLUME_LIMIT = prefix + "max_limit_val"
        protected val IS_MAX_VOLUME_LIMITED = prefix + "max_limit_enabled"

        protected val IS_MIN_VOLUME_LIMITED = prefix + "min_limit_enabled"
        protected val USER_MIN_VOLUME_LIMIT = prefix + "min_limit_val"

        protected val PAUSE_WA_ENABLED = prefix + "pause_wa"
        protected val PAUSE_WA_TIME_TO_WAIT = prefix + "pause_wa_time"

        protected val RESTORE_TO_USER_LVL_ENABLED = prefix + "RESTORE_TO_USER_LVL_ENABLED"
        protected val USER_SET_SOUND_LVL = prefix + "user set level  val"

        protected val RING_WHEN_MUTE = prefix + "ring_when_mute"

        protected val RUN_FOREGROUND = prefix + "RUN_FOREGROUND"
        protected val FOREGROUND_NOTIFICATION_WITH_MIN_PRIORITY = prefix + "FOREGROUND_MIN_PRIORITY"

        protected val SOUND_STREAM_KEY = prefix + "sound_stream"

        protected val VOL_DONW_ASAP = prefix + "first_vol_down_asap"

        protected val LOGGING_ENABLED = prefix + "logging_enabled"

        protected val NOTIFICATION_USE_LED = prefix + "notification_use_led"
        protected val USE_FLASH_LIGHT = prefix + "USE_FLASH_LIGHT"

        protected val VIBRATE_TIMES = prefix + "vibrate_times_count"
        protected val VIBRATE_BEFORE_RING = prefix + "vibrate_before_ring"

        protected val MUTE_TIMES = prefix + "mute_times_count"
        protected val MUTE_BEFORE_RING = prefix + "mute_before_ring"

        protected val FIND_PHONE_FUNCTION_STATUS = prefix + "find_phone_enabled"
        protected val FIND_PHONE_TIMES_BEFORE_MAXIMISE = prefix + "fin_phone_times_before_maximise"
        protected val SKIP_RING = prefix + "do_not_ring"
        protected val USE_SIMPLE_MODE = prefix + "use_simple_mode"

        val DEFAULT_INTERVAL_VALUE = 5

    }
}
