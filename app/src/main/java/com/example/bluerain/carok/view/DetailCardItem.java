package com.example.bluerain.carok.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bluerain.carok.R;
import com.example.bluerain.carok.base.BaseViewModel;
import com.example.bluerain.carok.enties.CardDateInfo;

/**
 * Created by bluerain on 17-2-8.
 */

public class DetailCardItem extends BaseViewModel<CardDateInfo> {

    private TextView mTxvCardDataStart;
    private TextView mTxvCardDataEnd;
    private TextView mTxvCardDataState;

    @Override
    protected int onGetResourceID() {
        return R.layout.item_lv_main_carinfo;
    }

    @Override
    protected void onInitialView(View rootView) {
        mTxvCardDataStart = (TextView) rootView.findViewById(R.id.txv_card_data_start);
        mTxvCardDataEnd = (TextView) rootView.findViewById(R.id.txv_card_data_end);
        mTxvCardDataState = (TextView) rootView.findViewById(R.id.txv_card_data_state);

    }

    @Override
    protected void onBindView(View rootView, CardDateInfo data, LayoutInflater inflater) {
        mTxvCardDataStart.setText(data.startDate);
        mTxvCardDataEnd.setText(data.endDate);
        mTxvCardDataState.setText(data.isPass() ? "通过" : "审核中");
    }
}
