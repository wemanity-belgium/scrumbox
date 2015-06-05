package com.wemanity.scrumbox.android.gui.member;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.adapter.AbstractSwipeEntityAdapter;
import com.wemanity.scrumbox.android.gui.base.adapter.ActionViewId;

import java.util.Arrays;
import java.util.List;

public class MemberSwipeAdapter extends AbstractSwipeEntityAdapter<Member> {
    public MemberSwipeAdapter(Context mContext, List<Member> objects) {
        super(mContext,R.layout.member_listview_custom_view, objects);
    }

    public MemberSwipeAdapter(Context mContext) {
        super(mContext, R.layout.member_listview_custom_view);
    }

    @Override
    protected void fillFrontValues(int i, View view) {
        Member member = getItem(i);
        TextView memberNickName = (TextView) view.findViewById(R.id.memberNickNameTextView);
        memberNickName.setText(member.getNickname());
    }

    @Override
    protected List<ActionViewId> getActionBottomViewIds() {
        return Arrays.asList(new ActionViewId(R.id.editImageView, EntityAction.UPDATE), new ActionViewId(R.id.deleteImageView, EntityAction.DELETE));
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.memberSwipeLayout;
    }
}
