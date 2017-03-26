package com.baybaka.incomingcallsound.ui.cards.main

import android.Manifest
import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Build
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SeekBar
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import butterknife.OnClick
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2
import com.baybaka.incomingcallsound.ui.cards.Restrict
import com.baybaka.incomingcallsound.ui.rv.RVAdapter
import com.baybaka.increasingring.utils.PermissionChecker
import com.baybaka.increasingring.utils.PermissionChecker.Version.android7plus
import com.baybaka.increasingring.utils.PermissionChecker.Version.isAndroid5AndLower
import com.baybaka.increasingring.utils.PermissionChecker.Version.isAndroid6


class VibrateMuteConfigCard : ListCardItem_v2() {
    val checker by lazy { PermissionChecker(activity()!!) }

    override val head = R.string.card_description_vibrate_head
    override val layout = R.layout.card_config_vibrate
    override val description = versionBasedDescription()
    override val descriptionFull = R.string.card_description_vibrate_full

    private fun versionBasedDescription():  Int = when {
        PermissionChecker.Version.isAndroid5AndLower() -> R.string.card_description_vibrate_short_android_4_5
        PermissionChecker.Version.isAndroid6() -> R.string.card_description_vibrate_short_android_6
        else -> R.string.card_description_vibrate_short_android_7
    }

    @Bind(R.id.set_vibrate_first_toggle)
    internal lateinit var vibrateSwitch: SwitchCompat

    @Bind(R.id.vibrate_time_textview)
    internal lateinit var vibrateCountTextView: TextView

    @Bind(R.id.chosen_stream_textview)
    internal lateinit var chosenStreamTextView: TextView

    @Bind(R.id.vibrate_times_seekbar)
    internal lateinit var vibrateTimesSeekbar: SeekBar

    @Bind(R.id.stream_switch)
    internal lateinit var streamSwitch: SwitchCompat

    @Bind(R.id.mute_switch)
    internal lateinit var muteSwitch: SwitchCompat

    @Bind(R.id.mute_time_textview)
    internal lateinit var muteCountTextView: TextView

    @Bind(R.id.mute_times_seekbar)
    internal lateinit var muteTimesSeekbar: SeekBar

    @Bind(R.id.stream_warning)
    internal lateinit var streamWarningText: TextView

    @Bind(R.id.vibrate_mute_warning)
    internal lateinit var muteVibrateWarning: TextView

    override fun init(view: View) {
        super.init(view)
        ButterKnife.bind(this, view)

        val stream = mSharedPreferenceController.soundStream

        val checked = stream == AudioManager.STREAM_MUSIC
        streamSwitch.isChecked = checked
        updateStreamDescription(checked)


        initVibrate()
        initMute()

        setMusicStreamWarning()
        setVibrateMuteWarning()
    }

    private fun setToPlayAsMusic() = streamSwitch.isChecked

    private fun initVibrate() {

        vibrateSwitch.isChecked = mSharedPreferenceController.isVibrateFirst

        val savedVibrateTimes = mSharedPreferenceController.vibrateTimesCount
        setVibrateSeekBarText(savedVibrateTimes)

        vibrateTimesSeekbar.progress = savedVibrateTimes
        vibrateTimesSeekbar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == 0)
                    seekBar.progress = 1
                setVibrateSeekBarText(seekBar.progress)
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar) {}

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar) {
                setVibrateSeekBarText(seekBar.progress)
                mSharedPreferenceController.vibrateTimesCount = seekBar.progress
            }
        })
    }

    private fun initMute() {
        muteSwitch.isChecked = mSharedPreferenceController.isMuteFirst


        val savedMuteTimes = mSharedPreferenceController.muteTimesCount
        setMuteSeekbarText(savedMuteTimes)

        muteTimesSeekbar.progress = savedMuteTimes
        muteTimesSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == 0)
                    seekBar.progress = 1
                setMuteSeekbarText(seekBar.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                setMuteSeekbarText(seekBar.progress)
                mSharedPreferenceController.muteTimesCount = seekBar.progress
            }
        })
    }

    @OnClick(R.id.set_vibrate_first_toggle)
    fun toggleVibrateFirst() {
        mSharedPreferenceController.isVibrateFirst = vibrateSwitch.isChecked
        setVibrateSeekBarText(vibrateTimesSeekbar.progress)
    }

    @OnCheckedChanged(R.id.stream_switch)
    @OnClick(R.id.stream_switch)
    fun streamSwitchChanged() {

        mSharedPreferenceController.setSoundStream(streamSwitch.isChecked)
        updateStreamDescription(streamSwitch.isChecked)
        rvAdapter.notifyUpdate()

    }

    private fun setVibrateSeekBarText(value: Int) {
        vibrateCountTextView.text = vibrateSeekText(value)
    }

    private fun vibrateSeekText(value: Int): String {
        if (vibrateSwitch.isChecked) {
            return context().getString(R.string.vibrate_time_desc, value)
        } else
            return context().getString(R.string.vibrate_disabled_desc)
    }

    override fun link(rvAdapter: RVAdapter) {
        this.rvAdapter = rvAdapter
    }


    private fun updateStreamDescription(nonDefault: Boolean) {
        val textId = if (nonDefault) R.string.music_stream_text else R.string.ring_stream_text
        chosenStreamTextView.setText(textId)
    }


    enum class HowBad {
        GOOD,
        WARNING,
        ERROR
    }

    private fun setState(v: TextView, state: HowBad) {
        when (state) {
            HowBad.GOOD -> v.visibility = GONE
            HowBad.WARNING -> {
                v.visibility = VISIBLE
                v.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_black_24dp, 0, 0, 0);
            }
            HowBad.ERROR -> {
                v.visibility = VISIBLE
                v.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_highlight_off_black_24dp, 0, 0, 0);
            }
        }
    }

    private fun ban(sw: SwitchCompat) = sw.let {
        it.isChecked = false
        it.isClickable = false
        it.alpha = 0.2f
    }

    private fun unBan(sw: SwitchCompat) = sw.let {
        it.isClickable = true
        it.alpha = 1f
    }

    @SuppressLint("NewApi")
    private fun setMusicStreamWarning() {
        val items: Array<SwitchCompat> = arrayOf(streamSwitch)
        val view = streamWarningText
        val readSd by lazy { checker.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) }
        val readContacts by lazy { checker.isGranted(Manifest.permission.READ_CONTACTS) }

        return when {
            isAndroid5AndLower() -> {
                setState(view, HowBad.GOOD)
                items.forEach { this::unBan }
            }

        //android 7+ can not mute ringtone without DnD control permission
            android7plus() && !checker.doNotDisturbGranted() -> {
                setState(view, HowBad.ERROR)
                items.forEach { ban(it) }
                view.setText(R.string.error_music_stream_android7_dnd_reason)
                view.setOnClickListener { checker.askForDndPermission() }
            }

        /*
        * in all other case we need permissions:
        * 1) sd read - to play custom ringtone from sd
        * 2) read contact - to find custom ringtone for contact
        * */
            readSd && readContacts -> {
                setState(view, HowBad.GOOD)
                items.forEach { unBan(it) }
            }
        //can play but will use default ringtone
            readSd && !readContacts -> {
                setState(view, HowBad.WARNING)
                items.forEach { unBan(it) }
                view.setText(R.string.permission_read_contacts)

                view.setOnClickListener { checker.checkMusicAndAsk() }
            }

        //!readSd && readContacts or !readSd && !readContacts -> cannot play if default ringtone on sd
            else -> {
                setState(view, HowBad.ERROR)
                items.forEach { ban(it) }
                view.setText(R.string.permission_read_sd)
                view.setOnClickListener { checker.checkMusicAndAsk() }
            }
        }

    }

    @SuppressLint("NewApi")
    private fun setVibrateMuteWarning() {
        val items: Array<SwitchCompat> = arrayOf(muteSwitch, vibrateSwitch)
        val view = muteVibrateWarning

        when {
        //should it also include 4.1 - 4.4  ?
            Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || isAndroid5AndLower() -> {

                if (setToPlayAsMusic()) {
                    setState(view, HowBad.GOOD)

                } else {
                    setState(view, HowBad.WARNING)
                    view.setText(R.string.warning_mute_vibrate_android4_and_5)
                }

                items.forEach { unBan(it) }

            }
            isAndroid6() -> {
                setState(view, HowBad.WARNING)
                items.forEach { unBan(it) }
                view.setText(R.string.warning_mute_vibrate_android6)
                view.setOnClickListener { checker.askForDndPermission() }
            }
        // now we have Android 7+ left
            checker.doNotDisturbGranted() -> {
                setState(view, HowBad.GOOD)
                items.forEach { unBan(it) }
            }
            else -> {
                setState(view, HowBad.ERROR)
                items.forEach { ban(it) }
                view.setText(R.string.error_mute_vibrate_android7)
                view.setOnClickListener { checker.askForDndPermission() }
            }
        }

    }


    @SuppressLint("NewApi")
    private fun canUseVibration(): Pair<Restrict, View> {
        val item = streamSwitch

        return when {
            Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                    Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 -> (Restrict.WARNING to item)
            checker.doNotDisturbGranted() -> (Restrict.NONE to item)
            else -> (Restrict.ERROR to item)
        }
    }


    @OnClick(R.id.mute_switch)
    fun toggleMute() {
        mSharedPreferenceController.isMuteFirst = muteSwitch.isChecked
        setMuteSeekbarText(muteTimesSeekbar.progress)
    }


    private fun setMuteSeekbarText(value: Int) {
        muteCountTextView.text = muteSeekText(value)
    }

    private fun muteSeekText(value: Int): String {
        if (muteSwitch.isChecked) {
            return context().getString(R.string.mute_time_desc, value)
        } else
            return context().getString(R.string.mute_disabled_desc)
    }

}
