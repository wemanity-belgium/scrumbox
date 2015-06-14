package com.wemanity.scrumbox.android.gui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.astuetz.PagerSlidingTabStrip;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.gui.daily.DailyMainFragment;
import com.wemanity.scrumbox.android.gui.member.MemberMainFragment;
import com.wemanity.scrumbox.android.gui.roti.RotiCalculatorFragment;

/**
 * Created by brixou on 07/06/15.
 */
public class RootPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private Context context;
    public RootPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new DailyMainFragment();
            case 1:
                // Games fragment activity
                return new MemberMainFragment();
            case 2:
                // Movies fragment activity
                return new RotiCalculatorFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sb;
        ImageSpan span;
        switch (position) {
            case 0:
                sb = new SpannableStringBuilder("Daily");
                span = new ImageSpan(context, R.drawable.daily_timer_logo, ImageSpan.ALIGN_BASELINE);
                break;
            case 1:
                sb = new SpannableStringBuilder("Member");
                span = new ImageSpan(context, R.drawable.member_logo, ImageSpan.ALIGN_BASELINE);
                break;
            case 2:
                sb = new SpannableStringBuilder("Roti");
                span = new ImageSpan(context, R.drawable.roti_logo, ImageSpan.ALIGN_BASELINE);
                break;
            default:
                sb = new SpannableStringBuilder("Default");
                span = new ImageSpan(context, R.drawable.scrum_poker_logo, ImageSpan.ALIGN_BASELINE);
                break;
        }

        sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public int getPageIconResId(int i) {
        switch (i) {
            case 0:
                return R.drawable.daily_timer_logo;
            case 1:
                return R.drawable.member_logo;
            case 2:
                return R.drawable.roti_logo;
            default:
                return R.drawable.scrum_poker_logo;
        }
    }
}
