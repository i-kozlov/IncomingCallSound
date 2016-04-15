/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baybaka.incomingcallsound.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.main.sliding.SlidingTabLayout;
import com.baybaka.incomingcallsound.ui.tabs.TabsFragmentFactory;


public class SlidingTabsBasicFragment extends Fragment {
    private static final int TOTAL_TABS_COUNT = 3;

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link android.support.v4.app.Fragment}, from the app's
     * resources.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs_holder, container, false);
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link android.support.v4.view.ViewPager}'s adapter to be an instance of {@link FragmentPagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link android.support.v4.view.ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);


        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)


    }
    // END_INCLUDE (fragment_onviewcreated)


    public void changeActiveTab(int tab) {
        mViewPager.setCurrentItem(tab);
    }
    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */


    class FragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter{

        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return TOTAL_TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getActivity().getString(R.string.tab_1_header);
                case 1:
                    return getActivity().getString(R.string.tab_2_header);

                case 2:
                    return getActivity().getString(R.string.tab_4_header);

            }

            return "Tab " + (position + 1);
        }

        @Override
        public Fragment getItem(int position) {
            return TabsFragmentFactory.createFragmentForPosition(position);
        }


    }
}
