package com.baybaka.incomingcallsound.ui.cards;

import com.baybaka.incomingcallsound.ui.cards.additional.IgnoreSilenceVibrateCard;
import com.baybaka.incomingcallsound.ui.cards.additional.KeepInMemoryCard;
import com.baybaka.incomingcallsound.ui.cards.additional.LoggingCard;
import com.baybaka.incomingcallsound.ui.cards.additional.PauseWaCard;
import com.baybaka.incomingcallsound.ui.cards.beta.FeedbackCard;
import com.baybaka.incomingcallsound.ui.cards.beta.FindPhoneConfigCard;
import com.baybaka.incomingcallsound.ui.cards.beta.MuteConfigCard;
import com.baybaka.incomingcallsound.ui.cards.beta.NotificationCard;
import com.baybaka.incomingcallsound.ui.cards.main.MainCard;
import com.baybaka.incomingcallsound.ui.cards.main.MinMaxLimitsCard;
import com.baybaka.incomingcallsound.ui.cards.main.OldMinMaxLimitsCard;
import com.baybaka.incomingcallsound.ui.cards.main.RestoreToLevelCard;
import com.baybaka.incomingcallsound.ui.cards.main.VibrateConfigCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardsFactory {

    public static List<ListCartItem> mainTab() {
        return Arrays.asList(
                new MainCard(),
                new MinMaxLimitsCard(),
                new RestoreToLevelCard()
        );
    }

    public static List<ListCartItem> additionalTab() {
        return Arrays.asList(
                new KeepInMemoryCard(),
                new IgnoreSilenceVibrateCard(),
                new VibrateConfigCard()
        );
    }

    public static List<ListCartItem> betaTab() {
        return Arrays.asList(
                new FeedbackCard(),
                new MuteConfigCard(),
                new NotificationCard(),
                new FindPhoneConfigCard()
        );
    }

    public static List<ListCartItem> old() {
        List<ListCartItem> list = new ArrayList<>();

        list.add(new MainCard());
        list.add(new OldMinMaxLimitsCard());
        list.add(new RestoreToLevelCard());
        list.add(new VibrateConfigCard());

        list.add(new IgnoreSilenceVibrateCard());
        list.add(new KeepInMemoryCard());
        list.add(new LoggingCard());
        list.add(new PauseWaCard());

        list.addAll(betaTab());
        return list;
    }

    public static List<ListCartItem> yesCard() {
        return Arrays.asList(
                new LoggingCard(),
                new PauseWaCard()
        );
    }

    public static List<ListCartItem> alarmTab() {
        return Arrays.asList(
                new MainCard(),
                new MinMaxLimitsCard(),
                new VibrateConfigCard(),
                new NotificationCard()
        );
    }

}
