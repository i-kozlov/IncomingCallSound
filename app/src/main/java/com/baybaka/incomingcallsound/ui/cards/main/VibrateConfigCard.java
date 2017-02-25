package com.baybaka.incomingcallsound.ui.cards.main;

import android.media.AudioManager;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCardItem;
import com.baybaka.incomingcallsound.ui.rv.RVAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VibrateConfigCard extends ListCardItem {

    @Bind(R.id.set_vibrate_first_toggle)
    SwitchCompat vibrateFirst;

    @Bind(R.id.vibrate_time_textview)
    TextView vibrateCountTextView;

    @Bind(R.id.chosen_stream_textview)
    TextView chosenStreamTextView;

    @Bind(R.id.vibrate_times_seekbar)
    SeekBar vibrateTimesSeekbar;
    @Bind(R.id.stream_switch)
    SwitchCompat streamSwitch;
    private RVAdapter rvAdapter;

    public VibrateConfigCard() {
        head = R.string.card_description_vibrate_head;
        layout = R.layout.card_config_vibrate;
        description = R.string.card_description_vibrate_short;
        descriptionFull = R.string.card_description_vibrate_full;
    }

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        vibrateFirst.setChecked(mSharedPreferenceController.isVibrateFirst());

        int stream = mSharedPreferenceController.getSoundStream();
        streamSwitch.setChecked(stream == AudioManager.STREAM_RING);
        updateStreamDescription(stream == AudioManager.STREAM_RING);

        int savedVibrateTimes = mSharedPreferenceController.getVibrateTimesCount();
        setSeekbarText(savedVibrateTimes);

        vibrateTimesSeekbar.setProgress(savedVibrateTimes);
        vibrateTimesSeekbar.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    seekBar.setProgress(1);
                setSeekbarText(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                setSeekbarText(seekBar.getProgress());
                mSharedPreferenceController.setVibrateTimesCount(seekBar.getProgress());
            }
        });
    }

    @OnClick(R.id.set_vibrate_first_toggle)
    public void toggleVibrateFirst() {
        mSharedPreferenceController.setVibrateFirst(vibrateFirst.isChecked());
        setSeekbarText(vibrateTimesSeekbar.getProgress());
    }

    @OnClick(R.id.stream_switch)
    public void streamSwitchChanged() {
        mSharedPreferenceController.setSoundStream(streamSwitch.isChecked());
        updateStreamDescription(streamSwitch.isChecked());

        MyApp.get().getListenerComponent().provideAudioWrapper().changeOutputStream(mSharedPreferenceController.getSoundStream());
        rvAdapter.notifyUpdate();
    }

    private void setSeekbarText(int value){
        vibrateCountTextView.setText(vibrateSeekText(value));
    }

    private String vibrateSeekText(int value) {
        if (vibrateFirst.isChecked()) {
            return MyApp.getContext().getString(R.string.vibrate_time_desc, value);
        } else return MyApp.getContext().getString(R.string.vibrate_disabled_desc);
    }

    @Override
    public void link(RVAdapter rvAdapter) {
        this.rvAdapter = rvAdapter;
    }

    private void updateStreamDescription(boolean isRingerStream) {
        if (isRingerStream) {
            chosenStreamTextView.setText(R.string.ring_stream_text);
        } else {
            chosenStreamTextView.setText(R.string.music_stream_text);
        }
    }
}
