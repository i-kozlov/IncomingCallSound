package com.baybaka.incomingcallsound.ui.cards.beta

import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2
import com.baybaka.increasingring.service.ServiceStarter
import org.slf4j.LoggerFactory

class FindPhoneConfigCard : ListCardItem_v2() {

    override val head = R.string.card_description_find_phone_head
    override val layout = R.layout.card_config_find_phone
    override val description = R.string.card_description_find_phone_short
    override val descriptionFull = R.string.card_description_find_phone_full

    @Bind(R.id.find_phone_switch)
    internal lateinit var findPhoneSwitch: SwitchCompat

    @Bind(R.id.find_phone_textview)
    internal lateinit var findPhoneTextView: TextView

    @Bind(R.id.find_phone_times_seekbar)
    internal lateinit var findPhoneTimesSeekbar: SeekBar


    override fun init(view: View) {

        ButterKnife.bind(this, view)
        findPhoneSwitch.isChecked = mSharedPreferenceController.isFindPhoneEnabled

        val ringTimes = mSharedPreferenceController.findPhoneCount
        setSeekbarText(ringTimes)

        findPhoneTimesSeekbar.progress = ringTimes
        findPhoneTimesSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == 0)
                    seekBar.progress = 1
                ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext())
                setSeekbarText(seekBar.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                setSeekbarText(seekBar.progress)
                mSharedPreferenceController.findPhoneCount = seekBar.progress
            }
        })


        findPhoneSwitch.setOnClickListener {
            mSharedPreferenceController.isFindPhoneEnabled = findPhoneSwitch.isChecked
            this@FindPhoneConfigCard.setSeekbarText(findPhoneTimesSeekbar.progress)
            LoggerFactory.getLogger(FindPhoneConfigCard::class.java.simpleName).info("findPhoneSwitch set {}. User call stopServiceRestartIfEnabled", findPhoneSwitch.isChecked)
            ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext())
        }
    }

    private fun setSeekbarText(value: Int) {
        findPhoneTextView.text = muteSeekText(value)
    }

    private fun muteSeekText(value: Int): String {
        return if (findPhoneSwitch.isChecked) MyApp.getContext().getString(R.string.find_phone_times_desc, value)
        else MyApp.getContext().getString(R.string.disabled)
    }

}
