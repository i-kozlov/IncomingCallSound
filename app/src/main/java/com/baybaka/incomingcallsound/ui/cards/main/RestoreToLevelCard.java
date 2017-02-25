package com.baybaka.incomingcallsound.ui.cards.main;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCardItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestoreToLevelCard extends ListCardItem {

    public RestoreToLevelCard() {
        head = R.string.card_description_restore_level_head;
        layout = R.layout.card_config_return_sound_to_level;
        description = R.string.card_description_restore_level_short;
        descriptionFull = R.string.card_description_restore_level_full;
    }

    @Bind(R.id.return_level_toggle)
    SwitchCompat mRestoreSoundToggle;
    @Bind(R.id.return_sound_description)
    TextView mRestoreSoundDescriptionText;
    @Bind(R.id.return_level_seekBar)
    SeekBar mRestoreSoundLevelSeek;

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        mRestoreSoundToggle.setChecked(mSharedPreferenceController.isRestoreVolToUserSetLevelEnabled());
        reconfigSeekbar();
    }

    @OnClick(R.id.return_level_toggle)
    public void switchReturnLevel() {
        mSharedPreferenceController.toggleRestoreVolToUserSetLevel(mRestoreSoundToggle.isChecked());
        mRestoreSoundLevelSeek.setEnabled(mRestoreSoundToggle.isChecked());
        changeRestoreSoundDescriptionText();
    }

    private void changeRestoreSoundDescriptionText() {
        if (mRestoreSoundToggle.isChecked()) {
            mRestoreSoundDescriptionText.setText(MyApp.getContext().getString(R.string.return_level_true_description, mRestoreSoundLevelSeek.getProgress()));
        } else {
            mRestoreSoundDescriptionText.setText(MyApp.getContext().getString(R.string.return_level_false_description));
        }
    }

    @Override
    public void update() {
        reconfigSeekbar();
    }

    private void reconfigSeekbar(){
        mRestoreSoundLevelSeek.setEnabled(mSharedPreferenceController.isRestoreVolToUserSetLevelEnabled());
        mRestoreSoundLevelSeek.setMax(MyApp.get().getMaxVol());
        mRestoreSoundLevelSeek.setProgress(mSharedPreferenceController.getUserSetLvl());
        changeRestoreSoundDescriptionText();
        mRestoreSoundLevelSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    seekBar.setProgress(1);
                changeRestoreSoundDescriptionText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newValue = seekBar.getProgress();

                changeRestoreSoundDescriptionText();
                mSharedPreferenceController.setUserSetLvl(newValue);

            }
        });
    }

}
