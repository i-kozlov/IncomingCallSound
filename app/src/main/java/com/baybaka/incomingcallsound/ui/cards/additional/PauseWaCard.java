package com.baybaka.incomingcallsound.ui.cards.additional;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PauseWaCard extends ListCartItem {

    public PauseWaCard() {
        head = R.string.card_description_pause_wa_head;
        layout = R.layout.card_config_pause_wa;
        description = R.string.card_description_pause_wa_short;
        descriptionFull = R.string.card_description_pause_wa_full;
    }


    @Bind(R.id.wa_pause_toggle)
    SwitchCompat pauseSwitch;
    @Bind(R.id.wa_pause_seekBar)
    SeekBar mPauseDurationSeek;


    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        pauseSwitch.setChecked(mSharedPreferenceController.isPauseWAenabled());

        final TextView pauseDescription = (TextView) view.findViewById(R.id.wa_pause_description);
        pauseDescription.setText(MyApp.getContext().getString(R.string.pause_before_restoring_description, mSharedPreferenceController.getPauseWAtimeValue()));

        mPauseDurationSeek.setMax(15);
        mPauseDurationSeek.setEnabled(mSharedPreferenceController.isPauseWAenabled());
        mPauseDurationSeek.setProgress(mSharedPreferenceController.getPauseWAtimeValue());
        mPauseDurationSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pauseDescription.setText(MyApp.getContext().getString(R.string.pause_before_restoring_description, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newValue = seekBar.getProgress();
                pauseDescription.setText(MyApp.getContext().getString(R.string.pause_before_restoring_description, newValue));
                mSharedPreferenceController.setPauseWAtimeValue(newValue);

            }
        });
    }

    @OnClick(R.id.wa_pause_toggle)
    public void pauseSwitchChanged() {
        mSharedPreferenceController.togglePauseWAState(pauseSwitch.isChecked());
        mPauseDurationSeek.setEnabled(pauseSwitch.isChecked());
    }

}
