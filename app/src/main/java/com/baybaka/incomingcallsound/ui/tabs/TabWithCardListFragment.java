package com.baybaka.incomingcallsound.ui.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCartItem;
import com.baybaka.incomingcallsound.ui.rv.RVAdapter;

import java.util.List;

public abstract class TabWithCardListFragment extends Fragment {

    protected TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_activity, container, false);
        mTextView = (TextView) view.findViewById(R.id.text_over_list);
        configText();

        rv=(RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
        return view;
    }

    protected void configText() {

    }

    private RecyclerView rv;
    List<ListCartItem> cards;

    private void initializeData() {
        cards = getCards();
    }

    protected abstract List<ListCartItem> getCards();


    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cards, getActivity());
        rv.setAdapter(adapter);
    }
}
