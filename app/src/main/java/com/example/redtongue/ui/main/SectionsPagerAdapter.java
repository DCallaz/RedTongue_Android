package com.example.redtongue.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.redtongue.MainActivity;
import com.example.redtongue.Progress;
import com.example.redtongue.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;
    private MainActivity m;
    public static Progress prog = null;

    public SectionsPagerAdapter(Context context, FragmentManager fm, MainActivity m) {
        super(fm);
        mContext = context;
        this.m = m;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return ModeFragment.newInstance(m);
        } else if (position == 1) {
            return PairFragment.newInstance(m);
        } else if (position == 2) {
            return FilesFragment.newInstance(m);
        } else if (position == 3) {
            TransferFragment t = TransferFragment.newInstance(m);
            this.prog = t;
            return t;
        } else {
            return PlaceholderFragment.newInstance(position);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return TAB_TITLES.length;
    }
}