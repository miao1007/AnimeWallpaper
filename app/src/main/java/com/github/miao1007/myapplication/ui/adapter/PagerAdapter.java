package com.github.miao1007.myapplication.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by leon on 11/24/14.
 */
public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    List<View> viewList;

    public PagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        super.instantiateItem(container, position);
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView(viewList.get(position));
    }
}
