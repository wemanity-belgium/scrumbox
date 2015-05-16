package com.wemanity.scrumbox.android.gui.base;

import android.app.Fragment;

import com.wemanity.scrumbox.android.R;

import roboguice.fragment.provided.RoboFragment;

public abstract class BaseFragment extends RoboFragment {

    public abstract Class<? extends BaseFragment> getPreviusFragment();
}
