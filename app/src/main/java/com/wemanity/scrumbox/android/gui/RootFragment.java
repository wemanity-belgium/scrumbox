package com.wemanity.scrumbox.android.gui;


import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.ScrumApplication;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.daily.DailyMainFragment;
import com.wemanity.scrumbox.android.gui.member.MemberMainFragment;
import com.wemanity.scrumbox.android.gui.roti.RotiCalculatorFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class RootFragment extends BaseFragment{

    @InjectView(R.id.mainPager)
    private ViewPager mainViewPager;
    @InjectView(R.id.tabs)
    private PagerSlidingTabStrip tabs;
    private RootPagerAdapter mAdapter;

    private final Handler handler = new Handler();

    private ActionBar actionBar;

    private Drawable oldBackground = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getActivity().getActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.root_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeColor(((ScrumApplication) getActivity().getApplication()).getDailyModule().getModuleColor());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new RootPagerAdapter(getChildFragmentManager(), getActivity());
        mainViewPager.setAdapter(mAdapter);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        mainViewPager.setPageMargin(pageMargin);

        tabs.setViewPager(mainViewPager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            ScrumApplication application = (ScrumApplication) getActivity().getApplication();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        changeColor(application.getDailyModule().getModuleColor());
                        break;
                    case 1:
                        changeColor(application.getMemberModule().getModuleColor());
                        break;
                    case 2:
                        changeColor(application.getRotiModule().getModuleColor());
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeColor(int newColor) {

        tabs.setIndicatorColor(newColor);
        // change ActionBar color just if an ActionBar is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Drawable colorDrawable = new ColorDrawable(newColor);
            Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
            LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

            if (oldBackground == null) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    ld.setCallback(drawableCallback);
                } else {
                    actionBar.setBackgroundDrawable(ld);
                }
            } else {

                TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

                // workaround for broken ActionBarContainer drawable handling on
                // pre-API 17 builds
                // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    td.setCallback(drawableCallback);
                } else {
                    actionBar.setBackgroundDrawable(td);
                }
                td.startTransition(200);

            }

            oldBackground = ld;
            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);

        }
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            actionBar.setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };


    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return null;
    }
}
