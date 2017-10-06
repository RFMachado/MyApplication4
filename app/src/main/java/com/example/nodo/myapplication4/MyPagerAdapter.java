package com.example.nodo.myapplication4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;

/**
 * Created by nodo on 06/10/17.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;




    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // Returns total number of pages
@Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return SearchFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 1
                return SearchFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 2
                return SearchFragment.newInstance(2, "Page # 3");
            default:
                return null;
        }

    }

}