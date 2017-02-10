package com.example.bluerain.carok.enties;

import com.example.bluerain.carok.base.CardAdapter;
import com.example.bluerain.carok.base.ICardAble;
import com.example.bluerain.carok.view.WeatherCard;

/**
 * Created by bluerain on 17-2-9.
 */

public class WeatherCardInfo implements ICardAble {
    public String xianxing_today;
    public String xianxing_tomorrow;
    public String weather;
    public String xiche;

    public WeatherCardInfo(String xianxing_today, String xianxing_tomorrow, String weather, String xiche) {
        this.xianxing_today = xianxing_today;
        this.xianxing_tomorrow = xianxing_tomorrow;
        this.weather = weather;
        this.xiche = xiche;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public CardAdapter onCreateAdapter() {
        return new WeatherCard();
    }
}
