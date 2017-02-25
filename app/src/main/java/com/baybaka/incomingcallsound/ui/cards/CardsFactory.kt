package com.baybaka.incomingcallsound.ui.cards

import android.os.Build
import com.baybaka.incomingcallsound.ui.cards.additional.IgnoreSilenceVibrateCard
import com.baybaka.incomingcallsound.ui.cards.additional.KeepInMemoryCard
import com.baybaka.incomingcallsound.ui.cards.additional.LoggingCard
import com.baybaka.incomingcallsound.ui.cards.additional.PauseWaCard
import com.baybaka.incomingcallsound.ui.cards.beta.FeedbackCard
import com.baybaka.incomingcallsound.ui.cards.beta.FindPhoneConfigCard
import com.baybaka.incomingcallsound.ui.cards.beta.MuteConfigCard
import com.baybaka.incomingcallsound.ui.cards.beta.NotificationCard
import com.baybaka.incomingcallsound.ui.cards.main.*
import java.util.*

object CardsFactory {

    fun mainTab(): List<ListCard> {

        return Arrays.asList(
                MainCard_v2(),
                MinMaxLimitsCard(),
                RestoreToLevelCard()
        )
    }

    fun additionalTab(): List<ListCard> {
        return Arrays.asList(
                KeepInMemoryCard(),
                IgnoreSilenceVibrateCard(),
                VibrateConfigCard()
        )
    }

    fun betaTab(): List<ListCard> {
        val preAndroid5 = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
        val list = mutableListOf<ListCard>(FeedbackCard())

        if (preAndroid5) list += MuteConfigCard()

        list += NotificationCard()
        list += FindPhoneConfigCard()
        return list
    }

    fun old(): List<ListCard> {
        val list = ArrayList<ListCard>()

        list.add(MainCard())
        list.add(OldMinMaxLimitsCard())
        list.add(RestoreToLevelCard())
        list.add(VibrateConfigCard())

        list.add(IgnoreSilenceVibrateCard())
        list.add(KeepInMemoryCard())
        list.add(LoggingCard())
        list.add(PauseWaCard())

        list.addAll(betaTab())
        return list
    }


    /**
     * Cards with default switch position = yes
     */
    fun yesCard(): List<ListCard> {
        return Arrays.asList(
                LoggingCard(),
                PauseWaCard()
        )
    }

    fun alarmTab(): List<ListCard> {
        return Arrays.asList(
                MainCard(),
                MinMaxLimitsCard(),
                VibrateConfigCard(),
                NotificationCard()
        )
    }

}
