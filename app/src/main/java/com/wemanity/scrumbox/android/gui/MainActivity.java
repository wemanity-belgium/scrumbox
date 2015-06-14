package com.wemanity.scrumbox.android.gui;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.ScrumApplication;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.main_layout)
public class MainActivity extends RoboFragmentActivity {

    @InjectView(R.id.mainPager)
    private ViewPager mainViewPager;
    @InjectView(R.id.tabs)
    private PagerSlidingTabStrip tabs;
    private RootPagerAdapter mAdapter;

    private final Handler handler = new Handler();


    private Drawable oldBackground = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        mAdapter = new RootPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        mainViewPager.setAdapter(mAdapter);


        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        mainViewPager.setPageMargin(pageMargin);

        tabs.setViewPager(mainViewPager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            ScrumApplication application = (ScrumApplication) getApplication();

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

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        changeColor(((ScrumApplication) getApplication()).getDailyModule().getModuleColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
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
                    getActionBar().setBackgroundDrawable(ld);
                }
            } else {

                TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

                // workaround for broken ActionBarContainer drawable handling on
                // pre-API 17 builds
                // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    td.setCallback(drawableCallback);
                } else {
                    getActionBar().setBackgroundDrawable(td);
                }
                td.startTransition(200);

            }

            oldBackground = ld;
            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

        }
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
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

}
