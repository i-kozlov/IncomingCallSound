package com.baybaka.incomingcallsound.ui.cards;

import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.log.logsender.EmailIntentCreator;
import com.baybaka.incomingcallsound.ui.rv.RVAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class BetaCard extends ListCardItem {

    @Nullable @Bind(R.id.send_email_beta_good)
    Button betaGood;

    @Nullable
    @Bind(R.id.send_email_beta_bad)
    Button betaBad;

    @Nullable @OnClick({ R.id.send_email_beta_good, R.id.send_email_beta_bad})
    public void sendEmail(Button button) {
        String buttonName = "hz button";
        if (button.getId() == R.id.send_email_beta_good) {
            buttonName = "Good button";
        } else if (button.getId() == R.id.send_email_beta_bad) {
            buttonName = "Bad button";
        }
        buttonName +=  " for " + rvAdapter.getContext().getString(getHead());
        try {
            rvAdapter.getContext().startActivity(EmailIntentCreator.getSendEmailIntent("Beta review", buttonName));
        } catch (Exception e) {
            Toast.makeText(MyApp.getContext(), R.string.error_noapp_tosend_email, Toast.LENGTH_LONG).show();
        }
    }

    protected RVAdapter rvAdapter;

    @Override
    public void link(RVAdapter rvAdapter) {
        this.rvAdapter = rvAdapter;
    }
}
