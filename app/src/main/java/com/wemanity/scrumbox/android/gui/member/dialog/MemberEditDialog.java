package com.wemanity.scrumbox.android.gui.member.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.util.StringUtils;

import roboguice.fragment.provided.RoboDialogFragment;
import roboguice.inject.InjectView;

public class MemberEditDialog extends RoboDialogFragment implements View.OnClickListener, View.OnKeyListener {
    private OnEntityChangeListener<Member> onEntityChangeListener;

    private boolean insertMode;

    @Inject
    private MemberDao memberDao;

    @InjectView(R.id.memberNicknameEditText)
    private EditText memberNicknameEdit;

    @InjectView(R.id.memberAvatarImageView)
    private ImageView memberAvatar;

    private Member member;

    public static MemberEditDialog newInstance(){
        MemberEditDialog frag = new MemberEditDialog();
        return frag;
    }

    public static MemberEditDialog newInstance(Member member){
        MemberEditDialog frag = new MemberEditDialog();
        Bundle args = new Bundle();
        args.putSerializable("member", member);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("member")) {
            member = (Member) args.get("member");
        }
        insertMode = (member == null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_member_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(getTitle());
        view.findViewById(R.id.positiveButton).setOnClickListener(this);
        view.findViewById(R.id.negativeButton).setOnClickListener(this);
        init();
    }

    private void init(){
        memberNicknameEdit.setOnKeyListener(this);
        if (!insertMode){
            memberNicknameEdit.setText(member.getNickname());
            memberNicknameEdit.setSelection(0, member.getNickname().length());
        }
    }

    private String getTitle(){
        String title;
        if (insertMode){
            title = getActivity().getString(R.string.new_member);
        } else {
            title = getActivity().getString(R.string.edit_member);
            title = String.format(title, member.getNickname());
        }
        return title;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positiveButton:
                if (!valideInput()){
                    break;
                }
            case R.id.negativeButton:
                dismiss();
        }
    }

    private boolean valideInput(){
        if (StringUtils.clear(memberNicknameEdit).isEmpty()){
            memberNicknameEdit.setError(getActivity().getString(R.string.nickname_empty));
            return false;
        }
        if (insertMode) {
            insertMember();
        } else{
            updateMember();
        }
        return true;
    }

    private void updateMember(){
        String newNickname = StringUtils.clear(memberNicknameEdit);
        if (!newNickname.equals(member.getNickname())){
            member.setNickname(newNickname);
            memberDao.update(member);
            notifyEntityChangeListener();
        }
    }

    private void insertMember(){
        member = new Member();
        member.setNickname(StringUtils.clear(memberNicknameEdit));
        memberDao.insert(member);
        notifyEntityChangeListener();
    }

    private void notifyEntityChangeListener(){
        if (onEntityChangeListener != null){
            onEntityChangeListener.onEntityChange(insertMode? EntityAction.INSERT : EntityAction.UPDATE, member);
        }
    }

    public OnEntityChangeListener<Member> getOnEntityChangeListener() {
        return onEntityChangeListener;
    }

    public void setOnEntityChangeListener(OnEntityChangeListener<Member> onEntityChangeListener) {
        this.onEntityChangeListener = onEntityChangeListener;
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
