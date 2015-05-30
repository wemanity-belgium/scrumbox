package com.wemanity.scrumbox.android.gui.member;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.AbstractEntityEditDialog;
import com.wemanity.scrumbox.android.gui.base.ImageReplacerView;
import com.wemanity.scrumbox.android.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import roboguice.inject.InjectView;

public class MemberEditDialog extends AbstractEntityEditDialog<Member> {
    private static final int CAMERA_PIC_REQUEST = 1313;
    @Inject
    private MemberDao memberDao;

    @InjectView(R.id.memberNicknameEditText)
    private EditText memberNicknameEdit;

    @InjectView(R.id.memberCameraView)
    private ImageReplacerView imageReplacerView;

    @InjectView(R.id.positiveButton)
    private TextView saveButton;

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
        imageReplacerView.saveDrawable(String.format("%s/member-avatar/%s.jpg",Environment.getExternalStorageDirectory().getAbsoluteFile(), member.getId()));
        return member;
    }
}
