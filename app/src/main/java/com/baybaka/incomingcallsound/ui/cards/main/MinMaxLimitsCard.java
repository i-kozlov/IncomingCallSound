package com.baybaka.incomingcallsound.ui.cards.main;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;
import com.baybaka.incomingcallsound.utils.Description;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MinMaxLimitsCard extends ListCartItem {

    public MinMaxLimitsCard() {
        head = R.string.card_description_config_min_max_head;
        layout = R.layout.card_config_min_max;
        description = R.string.card_description_config_min_max_short;
        descriptionFull = R.string.card_description_config_min_max_full;
    }

    @Override
    public void update() {
        maxHardwareLevel = MyApp.get().getMaxVol();
        if (rangebar != null) {
            reconfigureRangebar();
        }
    }

    private void reconfigureRangebar() {
        initRange();
        //!!! after init
        rangebar.setOnRangeBarChangeListener((rangeBar, indexL, indexR, lefttext, rightText) -> {

            int left = Integer.parseInt(lefttext);
            int right = Integer.parseInt(rightText);

            mSharedPreferenceController.setMinVolumeLimit(left);
            if (minLimitSwitch.isChecked()) {
                updateMinLimitText(getMinLimitText(left));
            }
            //minLimitChanged(left);

            mSharedPreferenceController.setMaxVolumeLimit(right);
            if (maxLimitSwitch.isChecked()) {
                updateMaxLimitText(getMaxLimitText(right));

            }
//                maxLimChanged(right);
        });
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
    @Bind(R.id.rangebar)
    RangeBar rangebar;

    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        maxHardwareLevel = MyApp.get().getMaxVol();
        reconfigureRangebar();
        minLimitSwitch.setChecked(mSharedPreferenceController.isMinVolumeLimited());
        maxLimitSwitch.setChecked(mSharedPreferenceController.isMaxVolumeLimited());

        setMinLimitDescriptionAccordingToSwitch();
        setMaxLimitDescAccordingToSwitch();
    }


    private final int RANGEBAR_MIN = -1;

    private void initRange() {
        rangebar.setTickStart(RANGEBAR_MIN);
        rangebar.setTickEnd(maxHardwareLevel + 1);
        loadRangebarValues();
    }

    private void loadRangebarValues() {
        int min = mSharedPreferenceController.getMinVolumeLimit();
        int max = mSharedPreferenceController.getMaxVolumeLimit();

        if (min < RANGEBAR_MIN) min = RANGEBAR_MIN;
        if (min > maxHardwareLevel + 1) min = maxHardwareLevel + 1;

        if (max < RANGEBAR_MIN) max = RANGEBAR_MIN;
        if (max > maxHardwareLevel + 1) max = maxHardwareLevel + 1;

        rangebar.setRangePinsByValue(min, max);
    }

    private void minLimitChanged(int newValue) {
        updateMinLimitText(getMinLimitText(newValue));
        mSharedPreferenceController.setMinVolumeLimit(newValue);
    }

    private void maxLimChanged(int newValue) {
        updateMaxLimitText(getMaxLimitText(newValue));
        mSharedPreferenceController.setMaxVolumeLimit(newValue);
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
//        rangebar.setEnabled(true);
        mSharedPreferenceController.enableMinVolumeLimit(minLimitSwitch.isChecked());
        setMinLimitDescriptionAccordingToSwitch();
    }

    @OnClick(R.id.enable_max_lovume_limit_toggle)
    public void switchMaxLimit() {
//        rangebar.setEnabled(true);
        mSharedPreferenceController.toggleMaxVolumeLimit(maxLimitSwitch.isChecked());
        setMaxLimitDescAccordingToSwitch();
    }

}
