package com.baybaka.incomingcallsound.ui.tabs;


import android.support.v4.app.Fragment;
import android.view.View;

import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.CardsFactory;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;
import com.baybaka.incomingcallsound.utils.Description;

import java.util.List;

public class TabsFragmentFactory {


    public static Fragment createFragmentForPosition(int position) {

        switch (position) {
            case 0:
                return new MainTab();

            case 1:
                return new AdditionalTab();

            default:
                return new BetaTab();
        }

    }

    public static class MainTab extends TabWithCardListFragment {
        @Override
        protected List<ListCartItem> getCards() {
            return CardsFactory.mainTab();
        }
    }

    public static class AdditionalTab extends TabWithCardListFragment {
        @Override
        protected List<ListCartItem> getCards() {
            return CardsFactory.additionalTab();
        }
    }

    public static class BetaTab extends TabWithCardListFragment {
        @Override
        protected List<ListCartItem> getCards() {
            return CardsFactory.betaTab();
        }
    }


    public static class YesCardsTab extends TabWithCardListFragment {
        @Override
        protected List<ListCartItem> getCards() {
            return CardsFactory.yesCard();
        }

        @Override
        protected void configText() {
            mTextView.setText(Description.getString(R.string.yes_page_text));
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    public static class OldList extends TabWithCardListFragment {
        @Override
        protected List<ListCartItem> getCards() {
            return CardsFactory.old();
        }
    }


}
