//package com.baybaka.incomingcallsound.ui.cards.beta
//
//import android.support.v7.widget.SwitchCompat
//import android.view.View
//import android.widget.SeekBar
//import android.widget.TextView
//import butterknife.Bind
//import butterknife.ButterKnife
//import butterknife.OnClick
//import com.baybaka.incomingcallsound.MyApp
//import com.baybaka.incomingcallsound.R
//import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2
//import com.baybaka.increasingring.utils.PermissionChecker
//
//class MuteConfigCard : ListCardItem_v2() {
//
//    override val head = R.string.card_description_mute_head
//    override val layout = R.layout.card_config_mute
//    override val description = R.string.card_description_mute_short
//    override val descriptionFull = R.string.card_description_mute_full
//
//    @Bind(R.id.mute_switch)
//    internal lateinit var muteSwitch: SwitchCompat
//
//    @Bind(R.id.mute_time_textview)
//    internal lateinit  var muteCountTextView: TextView
//
//    @Bind(R.id.mute_times_seekbar)
//    internal lateinit  var muteTimesSeekbar: SeekBar
//
//    override fun init(view: View) {
//        ButterKnife.bind(this, view)
//        muteSwitch.isChecked = mSharedPreferenceController.isMuteFirst
//
//
//        val savedMuteTimes = mSharedPreferenceController.muteTimesCount
//        setMuteSeekbarText(savedMuteTimes)
//
//        muteTimesSeekbar.progress = savedMuteTimes
//        muteTimesSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                if (progress == 0)
//                    seekBar.progress = 1
//                setMuteSeekbarText(seekBar.progress)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                setMuteSeekbarText(seekBar.progress)
//                mSharedPreferenceController.muteTimesCount = seekBar.progress
//            }
//        })
//    }
//
//    @OnClick(R.id.mute_switch)
//    fun toggleMute() {
//
//        val isOK = !muteSwitch.isChecked || activity()?.let {
//            return@let PermissionChecker(it).askForDndPermission()
//        } ?: false
//
//        rvAdapter.showText("Android 6 & 7 can not use Mute mode without providing do not disturb permission")
//
//        if(isOK) {
//            mSharedPreferenceController.isMuteFirst = muteSwitch.isChecked
//            setMuteSeekbarText(muteTimesSeekbar.progress)
//        } else {
//
//            muteSwitch.isChecked = !muteSwitch.isChecked
//        }
//
//    }
//
//
//    private fun setMuteSeekbarText(value: Int) {
//        muteCountTextView.text = muteSeekText(value)
//    }
//
//    private fun muteSeekText(value: Int): String {
//        if (muteSwitch.isChecked) {
//            return context().getString(R.string.mute_time_desc, value)
//        } else
//            return context().getString(R.string.mute_disabled_desc)
//    }
//
//}
