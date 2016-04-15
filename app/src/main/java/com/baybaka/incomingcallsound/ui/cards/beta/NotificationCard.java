package com.baybaka.incomingcallsound.ui.cards.beta;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.BetaCard;
import com.baybaka.incomingcallsound.utils.RateApp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationCard extends BetaCard {

    public NotificationCard() {
        head = R.string.card_description_notification_head;
        layout = R.layout.card_config_notification;
        description = R.string.card_description_notification_short;
        descriptionFull = R.string.card_description_notification_full;
        betaFeature = true;
    }

    @Bind(R.id.use_led_switch)
    SwitchCompat ledSwitch;

//    @Nullable
//    @Bind(R.id.use_flash_light_switch)
    SwitchCompat flashSwitch;

    @Bind(R.id.review)
    Button rateButton;

    @Override
    public void init(View view) {
        ButterKnife.bind(this, view);
        ledSwitch.setChecked(mSharedPreferenceController.isUseLed());
        if (flashSwitch!=null) {
            flashSwitch.setChecked(mSharedPreferenceController.isUsingFlash());
        }
    }

    @OnClick(R.id.use_led_switch)
    public void switchLedUsage() {
        mSharedPreferenceController.setUseLed(ledSwitch.isChecked());
    }

//    @OnClick(R.id.use_flash_light_switch)
//    public void switchFlashUsage() {
//        mSharedPreferenceController.setUsingFlash(flashSwitch.isChecked());
//    }

    @OnClick((R.id.review))
    public void rateApp() {
        RateApp.open(MyApp.getContext());
    }

}
