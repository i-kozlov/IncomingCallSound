package com.baybaka.incomingcallsound.ui.cards

import android.view.View
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.settings.AllSettings
import com.baybaka.incomingcallsound.ui.rv.RVAdapter


abstract class ListCardItem_v2() : ListCard {

    companion object {
        val mSharedPreferenceController: AllSettings = MyApp.get().listenerComponent.settings()
    }

    override fun update() {
    }

    override fun link(rvAdapter: RVAdapter) {
    }

    override fun init(view: View) {
    }

}


