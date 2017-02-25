package com.baybaka.incomingcallsound.ui.cards

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
//        val main = MainCard_v2(R.string.card_description_main_interval_head,
//                R.layout.card_config_service_interval,
//                R.string.card_description_main_interval_short,
//                R.string.card_description_main_interval_full
//        )

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
        return Arrays.asList(
                FeedbackCard(),
                MuteConfigCard(),
                NotificationCard(),
                FindPhoneConfigCard()
        )
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
