package com.baybaka.incomingcallsound.ui.rv;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baybaka.incomingcallsound.MyApp;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.cards.ListCard;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {

    public void notifyUpdate() {
        for (ListCard card : cards) {
                card.update();
        }
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        @Bind(R.id.beta_feature_lable)
        TextView betaLable;
        TextView top;
        TextView description;
        @Bind(R.id.full_description)
        TextView fullDescription;

        @Bind(R.id.expand_button)
        Button mButton;
        @Bind(R.id.expand)
        LinearLayout expandable;
        @Bind(R.id.expand_click)
        LinearLayout clickLayout;


        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cv = (CardView) itemView.findViewById(R.id.card_view);
            top = (TextView) itemView.findViewById(R.id.top);
            description = (TextView) itemView.findViewById(R.id.bottom);
        }

        @OnClick({R.id.expand, R.id.expand_button, R.id.expand_click})
        public void onClick(View v) {
//            Animation fade_in = AnimationUtils.loadAnimation(MyApp.getContext(), R.anim.fade_in);
//            Animation fade_out = AnimationUtils.loadAnimation(MyApp.getContext(), R.anim.fade_out);

            if (expandable.isShown()) {
//                    MyAnim.show(expandable, fade_in);
                MyAnim.collapse(expandable);
                //todo update on moving to 3.0
                mButton.setBackgroundDrawable(MyApp.getContext().getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));

            } else {
//                    MyAnim.hide(description, fade_out);
                MyAnim.expand(expandable);
                //todo update on moving to 3.0
                mButton.setBackgroundDrawable(MyApp.getContext().getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));

            }

        }
    }


    List<ListCard> cards;
    private Context context;

    public RVAdapter(List<ListCard> cards, Context context) {
        this.cards = cards;
        this.context = context;
        setHasStableIds(true);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        boolean beta = cards.get(viewType).isBetaFeature();
        if (beta) {
            {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_beta_template, viewGroup, false);
            }
        } else
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_template, viewGroup, false);

        ViewStub stub = (ViewStub) v.findViewById(R.id.stub);
        stub.setLayoutResource(cards.get(viewType).getLayout());
        stub.inflate();

        return new CardViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
//        return cards.get(position).isBetaFeature() ? 1 : 0;
        return position;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        ListCard card = cards.get(i);

        cardViewHolder.top.setText(fromId(cards.get(i).getHead()));
        cardViewHolder.description.setText(fromId(cards.get(i).getDescription()));
        cardViewHolder.fullDescription.setText(fromId(cards.get(i).getDescriptionFull()));

        int betaLabelVisibility = card.isBetaFeature() ? View.VISIBLE : View.GONE;
        cardViewHolder.betaLable.setVisibility(betaLabelVisibility);

        card.link(this);
        card.init(cardViewHolder.itemView);


    }

    @Override
    public long getItemId(int position) {
        return cards.get(position).getLayout();
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private String fromId(int id) {
        return MyApp.getContext().getString(id);
    }
}