package com.project.pseudotrade;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int TabNumber;

    public PageAdapter(FragmentManager fm, int TabNumber) {
        super(fm, TabNumber);
        this.TabNumber = TabNumber;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainPageFragment();

            //case 1:
                //return new Stock

            //case 2:
                //return new settings

            //case 3:
                //return Login

        }


        return null;
    }

    @Override
    public int getCount() {
        return TabNumber;
    }
}
