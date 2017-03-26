package com.baybaka.incomingcallsound.ui.cards.additional

import android.os.Build
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.LinearLayout
import butterknife.Bind
import butterknife.ButterKnife
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2
import com.baybaka.increasingring.service.ServiceStarter
import org.slf4j.LoggerFactory

class KeepInMemoryCard : ListCardItem_v2() {

    override val head = R.string.card_description_keep_in_mem__head
    override val layout = R.layout.card_config_keep_in_memory
    override val description = R.string.card_description_keep_in_mem_short
    override val descriptionFull = R.string.card_description_keep_in_mem_full

    @Bind(R.id.keep_in_memory_switch)
    internal lateinit var keepInMemorySwitch: SwitchCompat

    @Bind(R.id.keep_notification_priority)
    internal lateinit var keepPrioritySwitch: SwitchCompat

    @Bind(R.id.notification_min_priority_layout)
    internal lateinit var notificationMinPriorityLayout: LinearLayout

    override fun init(view: View) {
        super.init(view)
        ButterKnife.bind(this, view)
        keepInMemorySwitch.isChecked = mSharedPreferenceController.startInForeground()
        keepPrioritySwitch.isChecked = mSharedPreferenceController.showNotificationWithMinPriority()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notificationMinPriorityLayout.visibility = View.GONE
        }

        keepInMemorySwitch.setOnClickListener {
            mSharedPreferenceController.setRunForeground(keepInMemorySwitch.isChecked)
            LoggerFactory.getLogger(KeepInMemoryCard::class.java.simpleName).info("keepInMemorySwitch  set {}. User call stopServiceRestartIfEnabled", keepInMemorySwitch.isChecked)
            ServiceStarter.stopServiceRestartIfEnabled(appContext())
        }

        keepPrioritySwitch.setOnClickListener {
            mSharedPreferenceController.setMinNotificationPriory(keepPrioritySwitch.isChecked)
            LoggerFactory.getLogger(KeepInMemoryCard::class.java.simpleName).info("keepPrioritySwitch set {}. User call stopServiceRestartIfEnabled", keepPrioritySwitch.isChecked)
            ServiceStarter.stopServiceRestartIfEnabled(appContext())
        }
    }

}
