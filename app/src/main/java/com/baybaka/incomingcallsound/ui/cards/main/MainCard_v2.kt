package com.baybaka.incomingcallsound.ui.cards.main

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

class MainCard_v2 : ListCardItem_v2() {

    override val head = R.string.card_description_main_interval_head
    override val layout = R.layout.card_config_service_interval
    override val description = R.string.card_description_main_interval_short
    override val descriptionFull = R.string.card_description_main_interval_full

    @Bind(R.id.set_service_status_toggle)
    lateinit var mServiceRunningSwitch: SwitchCompat

    @Bind(R.id.interval_text)
    lateinit var intervalBatTextView: TextView


    override fun init(view: View) {
        super.init(view)
        ButterKnife.bind(this, view)

        mServiceRunningSwitch.isChecked = mSharedPreferenceController.isServiceEnabledInConfig

        val savedInterval = mSharedPreferenceController.interval
        intervalBatTextView.text = MyApp.getContext().getString(R.string.interval_text, savedInterval)

        val intervalSeek = view.findViewById(R.id.interval_seekBar) as SeekBar
        intervalSeek.progress = savedInterval

        intervalSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress == 0)
                    seekBar.progress = 1
                intervalBatTextView.text = MyApp.getContext().getString(R.string.interval_text, seekBar.progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val newValue = seekBar.progress
                val text = MyApp.getContext().getString(R.string.interval_text, newValue)
                intervalBatTextView.text = text
                mSharedPreferenceController.setNewIntervalValue(newValue)
            }
        })


        mServiceRunningSwitch.setOnClickListener { view ->
            val serviceStatus = mServiceRunningSwitch.isChecked
            mSharedPreferenceController.changeServiceEnabledSettings(serviceStatus)
            LoggerFactory.getLogger(MainCard_v2::class.java.simpleName).info("User set service status to $serviceStatus")
            ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext())
        }
    }

}
