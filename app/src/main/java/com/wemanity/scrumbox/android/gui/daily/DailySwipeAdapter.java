package com.wemanity.scrumbox.android.gui.daily;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.adapter.AbstractSwipeEntityAdapter;
import com.wemanity.scrumbox.android.gui.base.adapter.ActionViewId;

import java.util.Arrays;
import java.util.List;

public class DailySwipeAdapter extends AbstractSwipeEntityAdapter<Daily> {
    public DailySwipeAdapter(Context mContext, List<Daily> objects) {
        super(mContext,R.layout.daily_listview_custom_view, objects);
    }

    public DailySwipeAdapter(Context mContext) {
        super(mContext, R.layout.daily_listview_custom_view);
    }

    @Override
    protected void fillFrontValues(int i, View view) {
        Daily daily = getItem(i);
        TextView memberNickName = (TextView) view.findViewById(R.id.dailyTitleTextView);
        memberNickName.setText(daily.getTitle());
    }

    @Override
    protected List<ActionViewId> getActionBottomViewIds() {
        return Arrays.asList(new ActionViewId(R.id.editImageView, EntityAction.REPLACE), new ActionViewId(R.id.deleteImageView, EntityAction.DELETE), new ActionViewId(R.id.playImageView, EntityAction.START_DAILY));
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.dailySwipeLayout;
    }
}
