package com.wemanity.scrumbox.android.gui.base;

import roboguice.fragment.RoboFragment;

public abstract class BaseFragment extends RoboFragment {

    public abstract Class<? extends BaseFragment> getPreviusFragment();
}
