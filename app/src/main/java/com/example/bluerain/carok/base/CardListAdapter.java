package com.example.bluerain.carok.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluerain on 17-1-14.
 */

public class CardListAdapter extends BaseAdapter {


    private int mViewTypeCount;

    private Context mContext;

    private List<ICardAble> mCardAbles;


    public CardListAdapter(Context context) {
        mContext = context;
        initVariables();
    }

    public CardListAdapter(Context context, List data) {
        this(context);
        addAll(data);
    }

    private void initVariables() {
        mCardAbles = new ArrayList<>();
    }

    public void add(Object data) {
        if (null != data && data instanceof ICardAble) {
            ICardAble cur = (ICardAble) data;
            if (!mCardAbles.contains(cur)) {
                mCardAbles.add(cur);
            }
        }
    }

    public void add(int index, Object data) {
        if (null != data && data instanceof ICardAble) {
            ICardAble cur = (ICardAble) data;
            if (!mCardAbles.contains(cur)) {
                mCardAbles.add(index, cur);
            }
        }
    }

    public void addAll(List dataList) {
        if (null != dataList) {
            for (Object next : dataList) {
                add(next);
            }
        }
    }

    public void clear() {
        mCardAbles.clear();
    }


    public void setViewTypeCount(int viewTypeCount) {
        this.mViewTypeCount = viewTypeCount;
    }

    @Override
    public int getCount() {
        return mCardAbles.size();
    }

    @Override
    public ICardAble getItem(int position) {
        return mCardAbles.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return mViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        return mCardAbles.get(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            CardAdapter cardAdapter = mCardAbles.get(position).onCreateAdapter();
            if (cardAdapter != null) {
                if (cardAdapter instanceof BaseViewModel)
                    ((BaseViewModel) cardAdapter).setContext(mContext);

                v = cardAdapter.getItemView();
                if (v != null) {
                    v.setTag(cardAdapter);
                }
            }
        }
        if (v != null) {
            Object tagValue = v.getTag();
            if (tagValue != null && tagValue instanceof CardAdapter) {
                CardAdapter adapter = (CardAdapter) tagValue;
                adapter.bindView(v, getItem(position));
            }
        }
        return v;
    }

}
