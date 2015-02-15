package com.wemanity.scrumbox.android.gui.base;

import android.app.Fragment;

import com.wemanity.scrumbox.android.gui.base.BaseFragment;

public interface FragmentSwitchListener {
    void switchFragment(Fragment to, Fragment from) throws IllegalAccessException, InstantiationException;
}
