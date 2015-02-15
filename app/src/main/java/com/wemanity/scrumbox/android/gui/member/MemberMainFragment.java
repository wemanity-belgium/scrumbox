package com.wemanity.scrumbox.android.gui.member;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;

import java.util.List;

import roboguice.inject.InjectView;

public class MemberMainFragment  extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "DailyMainFragment";

    @Inject
    private MemberDao memberDao;

    @InjectView(R.id.memberListView)
    private ListView dailyListView;

    @InjectView(R.id.addMemberFAB)
    private FloatingActionButton dailyFAB;

    @InjectView(R.id.memberMainLayout)
    private RelativeLayout memberLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_main_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyFAB.setOnClickListener(this);
        List<Member> members = memberDao.queryBuilder().list();
        if (members.size()>0){
            String s = "";
        }
        //dailyFAB.setOnTouchListener(new MyTouchListener());
        //dailyLayout.setOnDragListener(new MyDragListener());
    }

    @Override
    public void onClick(View v) {
        new MaterialDialog.Builder(this.getActivity())
                .title(R.string.new_member)
                .customView(R.layout.new_member_dialog, true)
                .positiveText(R.string.action_add)
                .negativeText(R.string.action_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        View view = dialog.getCustomView();
                        TextView memberNicknameTextView = (TextView) view.findViewById(R.id.memberNicknameEditText);
                        ImageView memberAvatarImageView = (ImageView) view.findViewById(R.id.memberAvatarImageView);
                        String nickName = memberNicknameTextView.getText().toString();
                        Member newMember = new Member();
                        newMember.setNickname(nickName);
                        memberDao.insert(newMember);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        String s = "";
                    }
                })
                .build()
                .show();
    }
}