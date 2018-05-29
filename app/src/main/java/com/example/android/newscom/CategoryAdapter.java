package com.example.android.newscom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.newscom.fragments.BusinessFragment;
import com.example.android.newscom.fragments.EnvironmentFragment;
import com.example.android.newscom.fragments.HomeFragment;
import com.example.android.newscom.fragments.ScienceFragment;
import com.example.android.newscom.fragments.SportFragment;
import com.example.android.newscom.fragments.WorldFragment;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class CategoryAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context context;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    /**
     * Return page title of the tap
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        int titleId;
        switch (position) {
            case 0:
                titleId = R.string.title_home;
                break;
            case 1:
                titleId = R.string.title_world;
                break;
            case 2:
                titleId = R.string.title_science;
                break;
            case 3:
                titleId = R.string.title_sport;
                break;
            case 4:
                titleId = R.string.title_environment;
                break;
            case 5:
                titleId = R.string.title_business;
                break;
            default:
                titleId = R.string.title_home;
                break;
        }
        return context.getString(titleId);

    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 6;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new WorldFragment();
            case 2:
                return new ScienceFragment();
            case 3:
                return new SportFragment();
            case 4:
                return new EnvironmentFragment();
            case 5:
                return new BusinessFragment();
            default:
                return null;
        }
    }
}
