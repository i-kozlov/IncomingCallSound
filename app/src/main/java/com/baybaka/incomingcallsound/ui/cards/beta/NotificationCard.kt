package com.baybaka.incomingcallsound.ui.cards.beta

import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.Button
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.BetaCard_v2
import com.baybaka.incomingcallsound.utils.RateApp

class NotificationCard : BetaCard_v2() {

    override val head = R.string.card_description_notification_head
    override val layout = R.layout.card_config_notification
    override val description = R.string.card_description_notification_short
    override val descriptionFull = R.string.card_description_notification_full

    @Bind(R.id.use_led_switch)
    internal lateinit var ledSwitch: SwitchCompat

    //    @Nullable
    //    @Bind(R.id.use_flash_light_switch)
    internal  var flashSwitch: SwitchCompat? = null

    @Bind(R.id.review)
    internal lateinit var rateButton: Button

    override fun init(view: View) {
        ButterKnife.bind(this, view)
        ledSwitch.isChecked = mSharedPreferenceController.isUseLed()

        flashSwitch?.let {
            it.isChecked = mSharedPreferenceController.isUsingFlash
        }
    }

    @OnClick(R.id.use_led_switch)
    fun switchLedUsage() {
        mSharedPreferenceController.setUseLed(ledSwitch.isChecked)
    }

    //    @OnClick(R.id.use_flash_light_switch)
    //    public void switchFlashUsage() {
    //        mSharedPreferenceController.setUsingFlash(flashSwitch.isChecked());
    //    }

    @OnClick(R.id.review)
    fun rateApp() {
        RateApp.open(context())
    }

}
