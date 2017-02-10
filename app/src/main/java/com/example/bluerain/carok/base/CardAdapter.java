package com.example.bluerain.carok.base;

import android.view.View;

/**
 * Created by bluerain on 17-1-14.
 */

public interface CardAdapter {

    View getItemView();

    void bindView(View view, Object data);

}
