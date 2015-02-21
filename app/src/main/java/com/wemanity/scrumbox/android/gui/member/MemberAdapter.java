package com.wemanity.scrumbox.android.gui.member;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.adapter.EntityAdapter;

import java.util.List;

public class MemberAdapter extends EntityAdapter<Member> {

    private Drawable defaultAvatar;

    public MemberAdapter(Context context) {
        super(context, R.layout.member_listview_custom_view);
        init(null);
    }

    public MemberAdapter(Context context, List objects, Drawable defaultAvatar) {
        super(context, R.layout.member_listview_custom_view, objects);
        init(defaultAvatar);
    }

    public MemberAdapter(Context context, List objects) {
        super(context, R.layout.member_listview_custom_view, objects);
        init(null);
    }

    private void init(Drawable defaultAvatar){

        this.defaultAvatar = (defaultAvatar == null ? getContext().getResources().getDrawable(R.drawable.default_profile_avatar) : defaultAvatar);
    }

    @Override
    protected void populateView(View rowView, Member item) {
        TextView memberNickName = (TextView) rowView.findViewById(R.id.memberNickNameTextView);
        memberNickName.setText(item.getNickname());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.memberAvatarImageView);
        if (item.getImage() != null){
            //TODO
        } else{
            imageView.setBackground(defaultAvatar);
        }
    }
}
