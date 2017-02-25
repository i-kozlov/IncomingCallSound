package com.baybaka.incomingcallsound.ui.cards.beta;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.BetaCard;
import com.baybaka.increasingring.service.ServiceStarter;

import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindPhoneConfigCard extends BetaCard {

    @Bind(R.id.find_phone_switch)
    SwitchCompat findPhoneSwitch;

    @Bind(R.id.find_phone_textview)
    TextView findPhoneTextView;

    @Bind(R.id.find_phone_times_seekbar)
    SeekBar findPhoneTimesSeekbar;


    public FindPhoneConfigCard() {
        head = R.string.card_description_find_phone_head;
        layout = R.layout.card_config_find_phone;
        description = R.string.card_description_find_phone_short;
        descriptionFull = R.string.card_description_find_phone_full;
        betaFeature = true;
    }

    @Override
    public void init(View view) {

        ButterKnife.bind(this, view);
        findPhoneSwitch.setChecked(mSharedPreferenceController.isFindPhoneEnabled());

        int ringTimes = mSharedPreferenceController.getFindPhoneCount();
        setSeekbarText(ringTimes);

        findPhoneTimesSeekbar.setProgress(ringTimes);
        findPhoneTimesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    seekBar.setProgress(1);
                ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext());
                setSeekbarText(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setSeekbarText(seekBar.getProgress());
                mSharedPreferenceController.setFindPhoneCount(seekBar.getProgress());
            }
        });


        findPhoneSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferenceController.setFindPhoneEnabled(findPhoneSwitch.isChecked());
                FindPhoneConfigCard.this.setSeekbarText(findPhoneTimesSeekbar.getProgress());
                LoggerFactory.getLogger(FindPhoneConfigCard.class.getSimpleName()).info("findPhoneSwitch set {}. User call stopServiceRestartIfEnabled", findPhoneSwitch.isChecked());
                ServiceStarter.stopServiceRestartIfEnabled(MyApp.getContext());
            }
        });
    }

    private void setSeekbarText(int value) {
        findPhoneTextView.setText(muteSeekText(value));
    }

    private String muteSeekText(int value) {
        return findPhoneSwitch.isChecked() ? MyApp.getContext().getString(R.string.find_phone_times_desc, value) : MyApp.getContext().getString(R.string.disabled);
    }

}
