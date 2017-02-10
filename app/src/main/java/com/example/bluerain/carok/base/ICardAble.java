package com.example.bluerain.carok.base;

/**
 * Created by bluerain on 17-1-14.
 */

public interface ICardAble {

    boolean isValid();

    int getType();

    CardAdapter onCreateAdapter();
}
