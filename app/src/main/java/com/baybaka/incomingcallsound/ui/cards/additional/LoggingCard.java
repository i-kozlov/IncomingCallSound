package com.baybaka.incomingcallsound.ui.cards.additional;

import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoggingCard extends ListCartItem {

    public LoggingCard() {
        head = R.string.card_description_logging_head;
        layout = R.layout.card_config_logging;
        description = R.string.card_description_logging_short;
        descriptionFull = R.string.card_description_logging_full;
    }

    @Bind(R.id.enable_logging)
    SwitchCompat logSwitch;

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        logSwitch.setChecked(mSharedPreferenceController.isLoggingEnabled());
    }

    @OnClick((R.id.enable_logging))
    public void switchLogging() {
        mSharedPreferenceController.setLoggingState(logSwitch.isChecked());
    }
}
