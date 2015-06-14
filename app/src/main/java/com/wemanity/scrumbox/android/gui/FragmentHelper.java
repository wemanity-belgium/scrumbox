package com.wemanity.scrumbox.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wemanity.scrumbox.android.gui.base.BaseFragment;

public class FragmentHelper {

    public static void switchFragment(FragmentManager fm, Class<? extends BaseFragment> clazz, int layoutId, Bundle args){
        try{
            if (clazz == null){
                return;
            }
            FragmentTransaction ft = fm.beginTransaction();
            String toTag = clazz.getSimpleName();
            Fragment toShow = fm.findFragmentByTag(toTag);
            if (toShow == null){
                toShow = clazz.newInstance();
                toShow.setArguments(args);
                ft.add(layoutId, toShow, toTag);
            } else {
                //ft.show(toShow);
                ft.attach(toShow);
            }
            Fragment toHide = fm.findFragmentById(layoutId);
            if (toHide != null){
                ft.detach(toHide);
            }
            ft.commit();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void switchFragment(FragmentManager fm, Class<? extends BaseFragment> clazz, int layoutId){
        switchFragment(fm,clazz,layoutId,null);
    }
}
