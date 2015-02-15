package com.wemanity.scrumbox.android.gui.base;

import android.app.Fragment;

import com.wemanity.scrumbox.android.R;

import roboguice.fragment.provided.RoboFragment;

public class BaseFragment extends RoboFragment {


    public void switchFragment(Class<? extends Fragment> toClazz) {
        try{
            //TODO do not do a .remove(this) but a hide(this) and do a findFragmentByTag for the new fragment, if null do a add else do a show.
            Fragment to = toClazz.newInstance();
            getActivity().getFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(R.anim.enter_fragment,R.anim.exit_fragment,R.anim.pop_enter_fragment, R.anim.pop_exit_fragment)
                    .remove(this)
                    .add(R.id.fragment_frame_layout, to)
                    .commit();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
