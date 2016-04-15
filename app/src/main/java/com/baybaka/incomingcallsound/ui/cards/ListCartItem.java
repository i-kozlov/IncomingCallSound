package com.baybaka.incomingcallsound.ui.cards;

import android.view.View;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.settings.AllSettings;
import com.baybaka.incomingcallsound.ui.rv.RVAdapter;

public class ListCartItem {
    protected AllSettings mSharedPreferenceController = MyApp.get().getListenerComponent().settings();

    protected int head = R.string.limit_max_volume_toggle_text;
    protected int layout = R.layout.card_config_keep_in_memory;
    protected int description = R.string.descriptions_level_silence;
    protected int descriptionFull = R.string.volume_down;
    protected boolean betaFeature = false;

    public int getHead() {
        return head;
    }

    public int getLayout() {
        return layout;
    }

    public int getDescription() {
        return description;
    }

    public int getDescriptionFull() {
        return descriptionFull;
    }

    public boolean isBetaFeature() {
        return betaFeature;
    }


    public void init(View view){

    }

    /**
     * to get activity context
     * @param rvAdapter
     */
    public void link(RVAdapter rvAdapter) {

    }

    /**
     * to get update from recycler if needed
     */
    public void update(){}

}
