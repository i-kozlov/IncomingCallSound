package com.baybaka.incomingcallsound.ui.cards.main;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCardItem;
import com.baybaka.increasingring.service.ServiceStarter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainCard extends ListCardItem {

    public MainCard() {
        head = R.string.card_description_main_interval_head;
        layout = R.layout.card_config_service_interval;
        description = R.string.card_description_main_interval_short;
        descriptionFull = R.string.card_description_main_interval_full;
    }

    @Bind(R.id.set_service_status_toggle)
    SwitchCompat mServiceRunningSwitch;

    @Bind(R.id.interval_text)
    TextView intervalBatTextView;


    @Override
    public void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);

        mServiceRunningSwitch.setChecked(mSharedPreferenceController.isServiceEnabledInConfig());

        int savedInterval = mSharedPreferenceController.getInterval();
        intervalBatTextView.setText(MyApp.getContext().getString(R.string.interval_text, savedInterval));

        SeekBar intervalSeek = (SeekBar) view.findViewById(R.id.interval_seekBar);
        intervalSeek.setProgress(savedInterval);
        intervalSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    seekBar.setProgress(1);
                intervalBatTextView.setText(MyApp.getContext().getString(R.string.interval_text, seekBar.getProgress()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newValue = seekBar.getProgress();
                String text = MyApp.getContext().getString(R.string.interval_text, newValue);
                intervalBatTextView.setText(text);
                mSharedPreferenceController.setNewIntervalValue(newValue);
            }
        });

        mServiceRunningSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serviceStatus = mServiceRunningSwitch.isChecked();
                mSharedPreferenceController.changeServiceEnabledSettings(serviceStatus);
//                LoggerFactory.getLogger(MainCard.class.getSimpleName()).info("User set service status to {}", serviceStatus);
                ServiceStarter.INSTANCE.stopServiceRestartIfEnabled(MyApp.getContext());
            }
        });
    }

}
