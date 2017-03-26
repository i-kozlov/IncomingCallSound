package com.baybaka.incomingcallsound.ui.cards.additional

import android.support.v7.widget.SwitchCompat
import android.view.View
import butterknife.Bind
import butterknife.ButterKnife
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2

class IgnoreSilenceVibrateCard : ListCardItem_v2() {

    override val head = R.string.card_description_ignore_silence_vibrate_head
    override val layout = R.layout.card_config_ignore_silence_vibrate
    override val description = R.string.card_description_ignore_silence_vibrate_short
    override val descriptionFull = R.string.card_description_ignore_silence_vibrate_full

    @Bind(R.id.ignore_vibrate_toggle)
    internal lateinit var ignoreSilenceModeSwitch: SwitchCompat

    override fun init(view: View) {
        super.init(view)
        ButterKnife.bind(this, view)

        ignoreSilenceModeSwitch.isChecked = mSharedPreferenceController.ringWhenMute()
        ignoreSilenceModeSwitch.setOnClickListener {
            mSharedPreferenceController.toggleIgnoreSilenceVibrate(ignoreSilenceModeSwitch.isChecked) }
    }


}
