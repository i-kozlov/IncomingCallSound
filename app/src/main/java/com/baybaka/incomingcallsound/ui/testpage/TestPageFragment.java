package com.baybaka.incomingcallsound.ui.testpage;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.log.LogReceiver;
import com.baybaka.incomingcallsound.log.logsender.EmailIntentCreator;
import com.baybaka.increasingring.audio.IAudioController;
import com.baybaka.increasingring.receivers.TestPageOnCallEvenReceiver;
import com.baybaka.increasingring.settings.RunTimeSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestPageFragment extends Fragment  implements TestPageOnCallEvenReceiver.FragmentCallUpdater, LogReceiver.LogReceiveInterface {

    private static final Logger LOG = LoggerFactory.getLogger(TestPageFragment.class.getSimpleName());
    private static final String SAVE_INSTANCE_LABEL = "textview_save";

    @Bind(R.id.scroll_view)
    ScrollView mScrollView;
    @Bind(R.id.test_result_textview)
    TextView mTextView;

    @Bind(R.id.vol_but_check)
    TextView volButCheckTextview;
    @Bind(R.id.incalls_check)
    TextView inCallCheckTextview;
    @Bind(R.id.description_check)
    TextView decsriptionCheck;
    @Bind(R.id.feedback_text)
    EditText feedbackInput;

    @Bind(R.id.reset_config)
    Button resetConfig;

    private boolean buttonsCheckDone;
    private int buttonsPressed;

    private boolean inCallesCheckDone;
    private int callsCount;

    private boolean hasDescription;

    private BroadcastReceiver mReceiver;
    private BroadcastReceiver mReceiver2;


    private IAudioController mAudioController;
    private RunTimeSettings mRunTimeSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRunTimeSettings = MyApp.get().getRunTimeSettings();
        setRetainInstance(true);
        registerReceivers();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_page_layout, container, false);
        ButterKnife.bind(this, view);


        mRunTimeSettings.setTestPageOpened(true);
        mAudioController = MyApp.get().getListenerComponent().audioController();

        setupWidgets(view);

        if (savedInstanceState != null) {
            buttonsCheckDone = savedInstanceState.getBoolean("1");
            buttonsPressed = savedInstanceState.getInt("buttonsPressed", 0);
            buttonCheckCount();
            inCallesCheckDone = savedInstanceState.getBoolean("2");
            callsCount = savedInstanceState.getInt("callsCount", 0);
            callCountCheck();
            hasDescription = savedInstanceState.getBoolean("3");
            checkDescription();

            String text = savedInstanceState.getString(SAVE_INSTANCE_LABEL);
            mTextView.setText(text);
            scrollToEnd();
        }

        return view;
    }

    private void setupWidgets(View view) {
        mTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                scrollToEnd();
            }
        });

        Button volumeUp = (Button) view.findViewById(R.id.volume_up);
        Button volumeDown = (Button) view.findViewById(R.id.volume_down);
        Button sendResult = (Button) view.findViewById(R.id.send_result);

        volumeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestPageFragment.this.volumeUp();
            }
        });
        volumeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestPageFragment.this.volumeDown();
            }
        });
        sendResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestPageFragment.this.sendEmail();
            }
        });

        feedbackInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 3) {
                    hasDescription = true;
                }
                checkDescription();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }




    private void checkDescription() {
        if (hasDescription) {
            decsriptionCheck
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
        }
    }

    private void sendEmail() {
        if (buttonsCheckDone && inCallesCheckDone && hasDescription) {
            try {
                Intent sendEmailIntent = EmailIntentCreator.getSendEmailIntent("Test page report", feedbackInput.getText().toString(), getContext());
                startActivity(sendEmailIntent);
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.error_noapp_tosend_email, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.please_meet_all_conditions, Toast.LENGTH_LONG).show();
        }
    }

    private void registerReceivers() {
        IntentFilter logFilter = new IntentFilter(LogReceiver.LOG_RESP);
        logFilter.addCategory(LogReceiver.LOG_RESP);
        mReceiver = new LogReceiver(this);
        getActivity().registerReceiver(mReceiver, logFilter);

        IntentFilter callFilter = new IntentFilter(TestPageOnCallEvenReceiver.CALL_RECEIVER);
        callFilter.addCategory(TestPageOnCallEvenReceiver.CALL_RECEIVER);
        mReceiver2 = new TestPageOnCallEvenReceiver(this);
        getActivity().registerReceiver(mReceiver2, callFilter);
    }

    private void scrollToEnd() {
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void volumeUp() {
        buttonCheckCount();
        mTextView.append(mAudioController.getSTREAM_TYPE() + " level is " + getCurrentLevel() + "\n" + "++");
        scrollToEnd();
        LOG.info("user pushed volumeUpButton, sound level was {}", getCurrentLevel());
        setSoundLevel(getCurrentLevel() + 1);
    }

    private void volumeDown() {
        buttonCheckCount();
        mTextView.append(mAudioController.getSTREAM_TYPE() + " level is " + getCurrentLevel() + "\n" + "--");
        scrollToEnd();
        LOG.info("user pushed volumeDownButton, sound level was {}", getCurrentLevel());
        setSoundLevel(getCurrentLevel() - 1);
    }

    private void buttonCheckCount() {
        buttonsPressed++;
        if (buttonsPressed > 2) {
            buttonsCheckDone = true;
            volButCheckTextview
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
        }
    }

    private void setSoundLevel(int soundLevel) {
        if (soundLevel < 0)
            soundLevel = 0;

        mAudioController.new_SetAudioLevel(soundLevel);
        String txt = "user set " + mAudioController.getSTREAM_TYPE() + " level to " + getCurrentLevel() + "\n";
        mTextView.append(txt);
        LOG.info(txt);
    }

    private int getCurrentLevel() {
        return mAudioController.new_GetAudioLevel();
    }

    @Override
    public void onDestroy() {

        try {
            mRunTimeSettings.setTestPageOpened(false);
            if (getActivity() != null && mReceiver != null) {
                getActivity().unregisterReceiver(mReceiver);
                mReceiver = null;
            }
            if (getActivity() != null && mReceiver2 != null) {
                getActivity().unregisterReceiver(mReceiver2);
                mReceiver2 = null;
            }
        } catch (Exception e){
            LOG.error(e.getMessage());
        }

        super.onDestroy();
    }

    public void callReceived() {
        callsCount++;
        callCountCheck();
    }

    private void callCountCheck() {
        if (callsCount > 1) {
            inCallesCheckDone = true;
            inCallCheckTextview
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
        }
    }

    @OnClick(R.id.reset_config)
    public void resetConfig() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.reset_config_dialog_head))
                .setMessage(getString(R.string.reset_config_dialog_body))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TestPageFragment.this.resetPrefs();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void update() {
        callReceived();
    }

    private void resetPrefs(){
        MyApp.get().getListenerComponent().settings().resetConfig();
    }


    @Override
    public void logsToTextView(String logs) {
        mTextView.append(logs);
        mTextView.append("\n");
        scrollToEnd();
    }
}
