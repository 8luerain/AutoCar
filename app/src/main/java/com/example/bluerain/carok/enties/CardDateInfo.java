package com.example.bluerain.carok.enties;

import android.text.TextUtils;

import com.example.bluerain.carok.base.CardAdapter;
import com.example.bluerain.carok.base.ICardAble;
import com.example.bluerain.carok.utils.DateUtils;

/**
 * Created by bluerain on 17-2-8.
 */

public class CardDateInfo implements ICardAble {
    public String startDate;
    public String endDate;
    public String state;

    public CardDateInfo(String startDate, String endDate, String state) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public CardAdapter onCreateAdapter() {
        return null;
    }

    /**
     * 是否审核通过
     *
     * @return
     */
    public boolean isPass() {
        return TextUtils.equals(state, "1");
    }

    /**
     * 当前时间是否在有限日期内
     *
     * @return
     */
    public boolean isContainDate() {
        final long start = DateUtils.parseDate2Long(startDate+"-00-00-00");
        final long end = DateUtils.parseDate2Long(endDate+"-23-59-59");
        boolean max = System.currentTimeMillis() > start;
        boolean min = System.currentTimeMillis() < end;
        return max && min;
    }

}
