package com.dalpiazsolutions.coffeealarm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs)
    {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }
    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                TabCoffee tabCoffee = new TabCoffee();
                return tabCoffee;
            case 1:
                TabAlarm tabAlarm = new TabAlarm();
                return tabAlarm;
            case 2:
                TabLight tabLight = new TabLight();
                return tabLight;
            case 3:
                TabData tabData = new TabData();
                return tabData;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
