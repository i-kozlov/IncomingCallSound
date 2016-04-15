package com.baybaka.incomingcallsound.ui.cards.beta;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.BetaCard;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MuteConfigCard extends BetaCard{

    @Bind(R.id.mute_switch)
    SwitchCompat muteSwitch;

    @Bind(R.id.mute_time_textview)
    TextView muteCountTextView;

    @Bind(R.id.mute_times_seekbar)
    SeekBar muteTimesSeekbar;


    public MuteConfigCard() {
        head = R.string.card_description_mute_head;
        layout = R.layout.card_config_mute;
        description = R.string.card_description_mute_short;
        descriptionFull = R.string.card_description_mute_full;
        betaFeature= true;
    }

    @Override
    public void init(View view) {
        ButterKnife.bind(this, view);
        muteSwitch.setChecked(mSharedPreferenceController.isMuteFirst());



        int savedMuteTimes = mSharedPreferenceController.getMuteTimesCount();
        setSeekbarText(savedMuteTimes);

        muteTimesSeekbar.setProgress(savedMuteTimes);
        muteTimesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    seekBar.setProgress(1);
                setSeekbarText(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setSeekbarText(seekBar.getProgress());
                mSharedPreferenceController.setMuteTimesCount(seekBar.getProgress());
            }
        });
    }

    @OnClick(R.id.mute_switch)
    public void toogleMute() {
        mSharedPreferenceController.setMuteFirst(muteSwitch.isChecked());
        setSeekbarText(muteTimesSeekbar.getProgress());
    }


    private void setSeekbarText(int value){
        muteCountTextView.setText(muteSeekText(value));
    }

    private String muteSeekText(int value) {
        if (muteSwitch.isChecked()) {
            return MyApp.getContext().getString(R.string.mute_time_desc, value);
        } else return MyApp.getContext().getString(R.string.mute_disabled_desc);
    }

}
