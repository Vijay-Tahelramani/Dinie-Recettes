package com.example.dinierecettes;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class AddToFavViewPagerAdapter extends PagerAdapter {

    public Object instantiateItem(ViewGroup collection, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.add_to_fav_pager_one;
                break;
            case 1:
                resId = R.id.add_to_fav_pager_two;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
