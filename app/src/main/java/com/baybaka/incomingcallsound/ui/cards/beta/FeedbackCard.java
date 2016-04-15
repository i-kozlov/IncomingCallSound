package com.baybaka.incomingcallsound.ui.cards.beta;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.log.logsender.EmailIntentCreator;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;
import com.baybaka.incomingcallsound.ui.rv.RVAdapter;
import com.baybaka.incomingcallsound.ui.testpage.TestPageFragment;
import com.baybaka.incomingcallsound.utils.RateApp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackCard extends ListCartItem {

    private RVAdapter rvAdapter;

    public FeedbackCard() {
        head = R.string.card_description_feedback_head;
        layout = R.layout.card_config_feedback;
        description = R.string.card_description_feedback_short;
        descriptionFull = R.string.card_description_feedback_full;
    }

    @Bind(R.id.feedback_button)
    Button feedbackButton;
    @Bind(R.id.open_test_page)
    Button openTestPageButton;
    @Bind(R.id.rate_app_feedback)
    Button rateButton;
    @Bind(R.id.feedback_text)
    EditText feedbackTextInput;

    @Override
    public void init(View view) {
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.feedback_button)
    public void sendEmail() {
        try {
            rvAdapter.getContext().startActivity(EmailIntentCreator.getSendEmailIntent("From beta page", feedbackTextInput.getText().toString()));
        } catch (Exception e) {
            Toast.makeText(MyApp.getContext(), R.string.error_noapp_tosend_email, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.open_test_page)
    public void openTestPage() {
        ((AppCompatActivity)rvAdapter.getContext()).getSupportFragmentManager()
                .beginTransaction().
                replace(R.id.container, new TestPageFragment())
                .commit();
        ((AppCompatActivity)rvAdapter.getContext()).setTitle(rvAdapter.getContext().getResources().getString(R.string.test_page));
    }

    @OnClick(R.id.rate_app_feedback)
    public void rateApp() {
        RateApp.open(rvAdapter.getContext());
    }

    @Override
    public void link(RVAdapter rvAdapter) {
        this.rvAdapter = rvAdapter;
    }
}
