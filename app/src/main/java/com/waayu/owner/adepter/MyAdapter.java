package com.waayu.owner.adepter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.waayu.owner.fregment.HomeFragment;
import com.waayu.owner.fregment.ProductFragment;



public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment loanFragment = new HomeFragment();
                return loanFragment;

           /* case 1:
                CalculatorFragment historyFragment = new CalculatorFragment();
                return historyFragment;*/
            case 1:
                ProductFragment settingFragment = new ProductFragment();
                return settingFragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}