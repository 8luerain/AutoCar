package com.example.bluerain.carok.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bluerain.carok.MainActivity;
import com.example.bluerain.carok.R;
import com.example.bluerain.carok.base.BaseViewModel;
import com.example.bluerain.carok.enties.CardDateInfo;
import com.example.bluerain.carok.enties.Constance;
import com.example.bluerain.carok.enties.DetailCardInfo;
import com.example.bluerain.carok.event.OnItemClickEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by bluerain on 17-2-8.
 */

public class DetailCard extends BaseViewModel<DetailCardInfo> {
    private TextView mTvxCarNum;
    private TextView mTxvState;
    private ImageView mIgvAutoSend;
    private TextView mTvxValid;
    private TextView mTxvCanGo;
    private TextView mTxvTimeGo;
    private LinearLayout mContainer;

    @Override
    protected int onGetResourceID() {
        return R.layout.item_header_lv_main;
    }

    @Override
    protected void onInitialView(View rootView) {
        mTvxCarNum = (TextView) rootView.findViewById(R.id.tvx_car_num);
        mTvxValid = (TextView) rootView.findViewById(R.id.tvx_valid);
        mTxvState = (TextView) rootView.findViewById(R.id.txv_alarm_state);
        mIgvAutoSend = (ImageView) rootView.findViewById(R.id.igv_alarm_auto_send);
        mTxvCanGo = (TextView) rootView.findViewById(R.id.txv_can_go);
        mTxvTimeGo = (TextView) rootView.findViewById(R.id.txv_time_go);
        mContainer = (LinearLayout) rootView.findViewById(R.id.layout_info_container);

    }

    @Override
    protected void onBindView(View rootView, final DetailCardInfo data, LayoutInflater inflater) {
        mTvxCarNum.setText(data.carNum);
        mTvxValid.setEnabled(data.valid);
        mTvxValid.setText(data.valid ? "证件有效" : "失效");
        mTxvCanGo.setEnabled(true);
        switch (data.canGo) {
            case -1:
                mTxvCanGo.setText("error");
                break;
            case 0:
                mTxvCanGo.setText("不限行");
                break;
            case 1:
                mTxvCanGo.setEnabled(false);
                mTxvCanGo.setText("限行");
                break;
        }
        mTxvTimeGo.setEnabled(data.timeGo);
        mTxvTimeGo.setText(data.timeGo ? "非高峰" : "高峰");
        mContainer.removeAllViews();
        if (null != data.mCardDateInfos) {
            for (CardDateInfo info : data.mCardDateInfos) {
                final DetailCardItem cardData = new DetailCardItem();
                cardData.setContext(mContext);
                final View itemView = cardData.getItemView();
                cardData.bindView(itemView, info);
                mContainer.addView(itemView, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        mTxvState.setText(data.canSend);
        mIgvAutoSend.setImageResource(data.isAutoSend ? R.drawable.check_press : R.drawable.check_normal);
        mIgvAutoSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAutoSendClick(data);
            }
        });
    }

    /**
     * 当自动申请选中时
     *
     * @param data
     */
    private void handleAutoSendClick(DetailCardInfo data) {
        data.isAutoSend = !data.isAutoSend;
        mIgvAutoSend.setImageResource(data.isAutoSend ? R.drawable.check_press : R.drawable.check_normal);
        final SharedPreferences preferences = mContext.getSharedPreferences(Constance.CONFIG_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(DetailCardInfo.KEY_AUTO_SEND, data.isAutoSend).apply();
        EventBus.getDefault().post(new OnItemClickEvent(MainActivity.class.getName(), data));
    }
}
