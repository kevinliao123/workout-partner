package com.fruitguy.workoutpartner.main;

import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fruitguy.workoutpartner.chat.ChatFragment;
import com.fruitguy.workoutpartner.request.RequestFragment;
import com.fruitguy.workoutpartner.search.SearchFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by heliao on 2/6/18.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REQUEST, SEARCH, CHAT})

    public @interface FragmentPage{}

    public static final int REQUEST = 0;
    public static final int SEARCH = 1;
    public static final int CHAT = 2;

    List<Fragment> mFragmentList;
    public SectionPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragmentList = list;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
