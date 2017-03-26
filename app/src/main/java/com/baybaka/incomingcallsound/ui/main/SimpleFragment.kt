package com.baybaka.incomingcallsound.ui.main


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2
import com.baybaka.incomingcallsound.ui.cards.additional.KeepInMemoryCard
import com.baybaka.incomingcallsound.ui.cards.main.MainCard_v2
import com.baybaka.increasingring.service.ServiceStarter
import org.slf4j.LoggerFactory

class SimpleFragment : Fragment() {

    @Bind(R.id.set_service_status_toggle)
    lateinit var mServiceRunningSwitch: SwitchCompat

    @Bind(R.id.interval_text)
    lateinit var intervalBatTextView: TextView

    @Bind(R.id.keep_in_memory_switch)
    internal lateinit var keepInMemorySwitch: SwitchCompat

    fun context() = context

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.simple_mode, container, false)
        ButterKnife.bind(this, view)
        mServiceRunningSwitch.isChecked = ListCardItem_v2.mSharedPreferenceController.isServiceEnabledInConfig

        val savedInterval = ListCardItem_v2.mSharedPreferenceController.interval
        intervalBatTextView.text = context().getString(R.string.interval_text, savedInterval)

        val intervalSeek = view.findViewById(R.id.interval_seekBar) as SeekBar
        intervalSeek.progress = savedInterval

        intervalSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (progress < 1)
                    seekBar.progress = 1
                intervalBatTextView.text = context().getString(R.string.interval_text, seekBar.progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val newValue = seekBar.progress
                val text = context().getString(R.string.interval_text, newValue)
                intervalBatTextView.text = text
                ListCardItem_v2.mSharedPreferenceController.setNewIntervalValue(newValue)
            }
        })


        mServiceRunningSwitch.setOnClickListener { view ->
            val serviceStatus = mServiceRunningSwitch.isChecked
            ListCardItem_v2.mSharedPreferenceController.changeServiceEnabledSettings(serviceStatus)
            LoggerFactory.getLogger(MainCard_v2::class.java.simpleName).info("User set service status to $serviceStatus")
            ServiceStarter.stopServiceRestartIfEnabled(context)


        }


        keepInMemorySwitch.isChecked = ListCardItem_v2.mSharedPreferenceController.startInForeground()

        keepInMemorySwitch.setOnClickListener {
            ListCardItem_v2.mSharedPreferenceController.setRunForeground(keepInMemorySwitch.isChecked)
            LoggerFactory.getLogger(KeepInMemoryCard::class.java.simpleName).info("keepInMemorySwitch  set {}. User call stopServiceRestartIfEnabled", keepInMemorySwitch.isChecked)
            ServiceStarter.stopServiceRestartIfEnabled(context())
        }
        return view
    }

}
