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
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.ScrumApplication;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.main_layout)
public class MainActivity extends RoboFragmentActivity {

    private final Handler handler = new Handler();


    private Drawable oldBackground = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        FragmentHelper.switchFragment(this, RootFragment.class, R.id.fragmentFrameLayout);
   }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                BaseFragment frag = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentFrameLayout);
                if (frag != null){
                    FragmentHelper.switchFragment(this, frag.getPreviusFragment(), R.id.fragmentFrameLayout);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
