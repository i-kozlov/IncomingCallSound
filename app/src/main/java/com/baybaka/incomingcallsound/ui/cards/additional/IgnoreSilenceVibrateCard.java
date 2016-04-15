package com.baybaka.incomingcallsound.ui.cards.additional;

import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IgnoreSilenceVibrateCard extends ListCartItem {

    public IgnoreSilenceVibrateCard() {
        head = R.string.card_description_ignore_silence_vibrate_head;
        layout = R.layout.card_config_ignore_silence_vibrate;
        description = R.string.card_description_ignore_silence_vibrate_short;
        descriptionFull = R.string.card_description_ignore_silence_vibrate_full;
    }



    @Bind(R.id.ignore_vibrate_toggle)
    SwitchCompat ignoreSilenceModeSwitch;

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        ignoreSilenceModeSwitch.setChecked(mSharedPreferenceController.ringWhenMute());
        ignoreSilenceModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferenceController.toggleIgnoreSilenceVibrate(ignoreSilenceModeSwitch.isChecked());
            }
        });
    }


}
