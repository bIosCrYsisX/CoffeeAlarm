package com.dalpiazsolutions.coffeealarm;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
            case 4:
                TabAlert tabAlert = new TabAlert();
                return tabAlert;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
