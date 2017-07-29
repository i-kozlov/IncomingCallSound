package com.baybaka.incomingcallsound.ui.cards

import com.baybaka.incomingcallsound.ui.cards.additional.*
import com.baybaka.incomingcallsound.ui.cards.beta.FeedbackCard
import com.baybaka.incomingcallsound.ui.cards.beta.FindPhoneConfigCard
import com.baybaka.incomingcallsound.ui.cards.beta.NotificationCard
import com.baybaka.incomingcallsound.ui.cards.beta.NotificationLevelConfigCard
import com.baybaka.incomingcallsound.ui.cards.main.MainCard_v2
import com.baybaka.incomingcallsound.ui.cards.main.MinMaxLimitsCard
import com.baybaka.incomingcallsound.ui.cards.main.VibrateMuteConfigCard
import java.util.*

object CardsFactory {

    fun mainTab(): List<ListCard> = Arrays.asList(
            MainCard_v2(),
            MinMaxLimitsCard(),
            VibrateMuteConfigCard()

    )

    fun additionalTab(): List<ListCard> = Arrays.asList(
            KeepInMemoryCard(),
            IgnoreSilenceVibrateCard(),
            NotificationLevelConfigCard(),
            RestoreToLevelCard()
    )

    fun betaTab(): List<ListCard> {
//        val showMuteCard = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
//        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N || true

        val list = mutableListOf<ListCard>(FeedbackCard())

//         list += MuteConfigCard()

        list += FindPhoneConfigCard()
        list += NotificationCard()
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
//                MainCard(),
                MinMaxLimitsCard(),
                VibrateMuteConfigCard(),
                NotificationCard()
        )
    }

}
