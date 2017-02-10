package com.example.bluerain.carok.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by bluerain on 17-1-14.
 */

public abstract class BaseViewModel<T> implements CardAdapter {


    protected Context mContext;

    protected T mData;

    protected View mRootView;

    protected int mRes;

    protected LayoutInflater mLayoutInflater;


    public BaseViewModel() {

    }

    public void setRes(int res) {
        mRes = res;
    }

    public void setData(T data) {
        mData = data;
    }

    private View generateRootView() {
        View rootView = null;
        if (null != mLayoutInflater) {
            rootView = mLayoutInflater.inflate(onGetResourceID(), null);
        }

        return rootView;
    }

    public void setContext(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    protected abstract int onGetResourceID();

    protected abstract void onInitialView(View rootView);

    protected abstract void onBindView(View rootView, T data, LayoutInflater inflater);

    @Override
    public View getItemView() {
        mRootView = generateRootView();
        onInitialView(mRootView);
        return mRootView;
    }

    @Override
    public void bindView(View view, Object data) {
        mData = (T) data;
        onBindView(mRootView, mData, mLayoutInflater);
    }
}
