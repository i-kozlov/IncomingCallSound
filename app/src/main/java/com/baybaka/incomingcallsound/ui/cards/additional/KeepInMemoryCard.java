package com.baybaka.incomingcallsound.ui.cards.additional;

import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;
import com.baybaka.increasingring.service.ServiceStarter;

import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class KeepInMemoryCard extends ListCartItem {

    public KeepInMemoryCard() {
        head = R.string.card_description_keep_in_mem__head;
        layout = R.layout.card_config_keep_in_memory;
        description = R.string.card_description_keep_in_mem_short;
        descriptionFull = R.string.card_description_keep_in_mem_full;
    }

    @Bind(R.id.keep_in_memory_switch)
    SwitchCompat keepInMemorySwitch;

    @Bind(R.id.keep_notification_priority)
    SwitchCompat keepPrioritySwitch;

    @Bind(R.id.notification_min_priority_layout)
    LinearLayout notificationMinPriorityLayout;

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        keepInMemorySwitch.setChecked(mSharedPreferenceController.startInForeground());
        keepPrioritySwitch.setChecked(mSharedPreferenceController.showNotificationWithMinPriority());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notificationMinPriorityLayout.setVisibility(View.GONE);
        }

        keepInMemorySwitch.setOnClickListener(v -> {
            mSharedPreferenceController.setRunForeground(keepInMemorySwitch.isChecked());
            LoggerFactory.getLogger(KeepInMemoryCard.class.getSimpleName()).info("User call stopServiceRestartIfEnabled");
            ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext());
        });

        keepPrioritySwitch.setOnClickListener(v->{
            mSharedPreferenceController.setMinNotificationPriory(keepPrioritySwitch.isChecked());
            LoggerFactory.getLogger(KeepInMemoryCard.class.getSimpleName()).info("User call stopServiceRestartIfEnabled");
            ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext());
        });
    }

}
