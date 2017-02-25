package com.baybaka.incomingcallsound.ui.cards.main;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCardItem;
import com.baybaka.incomingcallsound.utils.Description;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OldMinMaxLimitsCard extends ListCardItem {

    public OldMinMaxLimitsCard() {
        head = R.string.card_description_config_min_max_head;
        layout = R.layout.card_old_min_max;
        description = R.string.card_description_config_min_max_short;
        descriptionFull = R.string.card_description_config_min_max_full;
    }

    @Override
    public void update() {
        maxHardwareLevel = MyApp.get().getMaxVol();
        if (mMinVolumeLimitSeekBar != null && mMaxVolumeLimitSeekBar != null) {
            updateSeekBars();
        }
    }

    private int maxHardwareLevel;

    @Bind(R.id.set_min_limit_toggle)
    SwitchCompat minLimitSwitch;

    @Bind(R.id.enable_max_lovume_limit_toggle)
    SwitchCompat maxLimitSwitch;

    @Bind(R.id.start_from_min_description)
    TextView minLevelDescriptionTextview;
    @Bind(R.id.show_device_max_volume_level)
    TextView maxLimitTextview;

    @Bind(R.id.min_level_seekBar)
    SeekBar mMinVolumeLimitSeekBar;

    @Bind(R.id.max_level_seekBar)
    SeekBar mMaxVolumeLimitSeekBar;

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        maxHardwareLevel = MyApp.get().getMaxVol();

        minLimitSwitch.setChecked(mSharedPreferenceController.isMinVolumeLimited());
        maxLimitSwitch.setChecked(mSharedPreferenceController.isMaxVolumeLimited());

        mMinVolumeLimitSeekBar.setProgress(mSharedPreferenceController.getMinVolumeLimit());
        mMaxVolumeLimitSeekBar.setProgress(mSharedPreferenceController.getMaxVolumeLimit());

        updateSeekBars();

        mMinVolumeLimitSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                minLimitChanged(seekBar.getProgress());

            }
        });

        mMaxVolumeLimitSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                maxLimChanged(seekBar.getProgress());
            }
        });

    }

    private void updateSeekBars(){
        mMinVolumeLimitSeekBar.setMax(maxHardwareLevel + 1);
        mMaxVolumeLimitSeekBar.setMax(maxHardwareLevel + 1);

        setMinLimitDescriptionAccordingToSwitch();
        setMaxLimitDescAccordingToSwitch();
    }

    private void minLimitChanged(int newValue) {
        mSharedPreferenceController.setMinVolumeLimit(newValue);
        if (minLimitSwitch.isChecked()) {
            updateMinLimitText(getMinLimitText(newValue));
        }
    }

    private void maxLimChanged(int newValue) {
        mSharedPreferenceController.setMaxVolumeLimit(newValue);
        if (maxLimitSwitch.isChecked()) {
            updateMaxLimitText(getMaxLimitText(newValue));
        }
    }


    private void updateMinLimitText(String text) {
        minLevelDescriptionTextview.setText(text);
    }

    private void updateMaxLimitText(String text) {
        maxLimitTextview.setText(text);
    }

    ////////text
    private void setMinLimitDescriptionAccordingToSwitch() {
        if (minLimitSwitch.isChecked()) {
            updateMinLimitText(getMinLimitText(mSharedPreferenceController.getMinVolumeLimit()));
        } else {
            minLevelDescriptionTextview.setText(MyApp.getContext().getString(R.string.start_from_min_false_description));
        }
    }

    private void setMaxLimitDescAccordingToSwitch() {
        if (maxLimitSwitch.isChecked()) {
            updateMaxLimitText(getMaxLimitText(mSharedPreferenceController.getMaxVolumeLimit()));
        } else {
            maxLimitTextview.setText(MyApp.getContext().getString(R.string.max_sound_level_text_disabled));
        }
    }

    private String getMaxLimitText(int newValue) {
        return MyApp.getContext().getString(R.string.max_sound_level_text, Description.getVolumeLevelText(newValue, maxHardwareLevel));
    }

    private String getMinLimitText(int newValue) {
        return MyApp.getContext().getString(R.string.start_from_min_true_description, Description.getVolumeLevelText(newValue, maxHardwareLevel));
    }

    @OnClick(R.id.set_min_limit_toggle)
    public void switchMinLimit() {
        mSharedPreferenceController.enableMinVolumeLimit(minLimitSwitch.isChecked());
        setMinLimitDescriptionAccordingToSwitch();
    }

    @OnClick(R.id.enable_max_lovume_limit_toggle)
    public void switchMaxLimit() {
        mSharedPreferenceController.toggleMaxVolumeLimit(maxLimitSwitch.isChecked());
        setMaxLimitDescAccordingToSwitch();
    }

}
