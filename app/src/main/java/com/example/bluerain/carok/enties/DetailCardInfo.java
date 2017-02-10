package com.example.bluerain.carok.enties;

import com.example.bluerain.carok.base.CardAdapter;
import com.example.bluerain.carok.base.ICardAble;
import com.example.bluerain.carok.view.DetailCard;

import java.util.List;

/**
 * Created by bluerain on 17-2-8.
 */

public class DetailCardInfo implements ICardAble {
    public static final String KEY_AUTO_SEND = "auto_send";

    public String carNum;
    public boolean valid;
    public int canGo;
    public boolean timeGo;
    public boolean isAutoSend;
    public String canSend;

    public List<CardDateInfo> mCardDateInfos;

    public DetailCardInfo(String carNum, boolean valid, int canGo) {
        this.carNum = carNum;
        this.valid = valid;
        this.canGo = canGo;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public CardAdapter onCreateAdapter() {
        return new DetailCard();
    }
}
