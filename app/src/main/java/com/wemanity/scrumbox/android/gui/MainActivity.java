package com.wemanity.scrumbox.android.gui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.main_layout)
public class MainActivity extends RoboActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(R.id.fragment_frame_layout, new RootFragment(), RootFragment.class.getSimpleName()).commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                BaseFragment frag = (BaseFragment) getFragmentManager().findFragmentById(R.id.fragment_frame_layout);
                if (frag != null){
                    FragmentHelper.switchFragment(getFragmentManager(), frag.getPreviusFragment(), R.id.fragment_frame_layout);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goBack(){

    }
}
