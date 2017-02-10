package com.example.bluerain.carok.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bluerain.carok.R;
import com.example.bluerain.carok.base.BaseViewModel;
import com.example.bluerain.carok.enties.WeatherCardInfo;

/**
 * Created by bluerain on 17-2-9.
 */

public class WeatherCard extends BaseViewModel<WeatherCardInfo> {
    private TextView mTxvWeatherXianxingToday;
    private TextView mTxvWeatherXianxingTomorrow;
    private TextView weatherXianxingW;
    private TextView txvWeatherXianxingXiche;

    @Override
    protected int onGetResourceID() {
        return R.layout.layout_weather_xianxing;
    }

    @Override
    protected void onInitialView(View rootView) {
        mTxvWeatherXianxingToday = (TextView) rootView.findViewById(R.id.txv_weather_xianxing_today);
        mTxvWeatherXianxingTomorrow = (TextView) rootView.findViewById(R.id.txv_weather_xianxing_tomorrow);
        weatherXianxingW = (TextView) rootView.findViewById(R.id.txv_weather_w);
        txvWeatherXianxingXiche = (TextView) rootView.findViewById(R.id.txv_weather_xiche);

    }

    @Override
    protected void onBindView(View rootView, WeatherCardInfo data, LayoutInflater inflater) {
        mTxvWeatherXianxingToday.setText("今日限行: " + data.xianxing_today);
        mTxvWeatherXianxingTomorrow.setText("明日限行: " + data.xianxing_tomorrow);
        weatherXianxingW.setText("今日天气: " + data.weather);
        txvWeatherXianxingXiche.setText("洗车指数: " + data.xiche);
    }
}
