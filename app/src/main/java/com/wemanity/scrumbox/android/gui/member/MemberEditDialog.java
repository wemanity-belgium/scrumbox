package com.wemanity.scrumbox.android.gui.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.AbstractEntityEditDialog;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.util.StringUtils;

import java.util.List;

import de.greenrobot.dao.query.WhereCondition;
import roboguice.fragment.provided.RoboDialogFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class MemberEditDialog extends AbstractEntityEditDialog<Member> implements View.OnKeyListener {
    private OnEntityChangeListener<Member> onEntityChangeListener;

    private boolean insertMode;

    @Inject
    private MemberDao memberDao;

    @InjectView(R.id.memberNicknameEditText)
    private EditText memberNicknameEdit;

    @InjectView(R.id.memberAvatarImageView)
    private ImageView memberAvatar;

    public static MemberEditDialog newInstance(){
        MemberEditDialog frag = new MemberEditDialog();
        return frag;
    }

    public static MemberEditDialog newInstance(Member member){
        MemberEditDialog frag = new MemberEditDialog();
        Bundle args = new Bundle();
        args.putSerializable("entity", member);
        frag.setArguments(args);
        return frag;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.new_member_dialog;
    }

    @Override
    protected String getTitle(boolean insertMode, Member entity){
        String title;
        if (insertMode){
            title = getActivity().getString(R.string.new_member);
        } else {
            title = getActivity().getString(R.string.edit_member);
            title = String.format(title, entity.getNickname());
        }
        return title;
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        memberNicknameEdit.setOnKeyListener(this);
    }

    @Override
    protected void bindEntity(View view, Bundle savedInstanceState, Member entity) {
        memberNicknameEdit.setText(entity.getNickname());
        memberNicknameEdit.setSelection(0, entity.getNickname().length());
    }

    @Override
    protected boolean valideInput(){
        if (StringUtils.clear(memberNicknameEdit).isEmpty()){
            memberNicknameEdit.setError(getActivity().getString(R.string.nickname_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void update(Member entity){
        String newNickname = StringUtils.clear(memberNicknameEdit);
        if (!newNickname.equals(entity.getNickname())){
            entity.setNickname(newNickname);
            memberDao.update(entity);
        }
    }

    @Override
    protected Member insert(){
        Member member = new Member();
        member.setNickname(StringUtils.clear(memberNicknameEdit));
        memberDao.insert(member);
        return member;
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (valideInput()){
                dismiss();
            }
            return true;
        }
        return false;
    }
}
