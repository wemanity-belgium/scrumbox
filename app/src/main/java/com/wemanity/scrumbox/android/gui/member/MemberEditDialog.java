package com.wemanity.scrumbox.android.gui.member;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.base.AbstractEntityEditDialog;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.base.ImageReplacerView;
import com.wemanity.scrumbox.android.util.StringUtils;

import de.greenrobot.dao.query.WhereCondition;
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
        String nickName = StringUtils.clear(memberNicknameEdit);
        if (StringUtils.clear(memberNicknameEdit).isEmpty()){
            memberNicknameEdit.setError(getActivity().getString(R.string.error_is_empty));
            return false;
        }
        long count = memberDao.queryBuilder().where(new WhereCondition.PropertyCondition(MemberDao.Properties.Nickname, "= ?", nickName)).count();
        if (count != 0){
            memberNicknameEdit.setError(getActivity().getString(R.string.error_nickname_use));
            memberNicknameEdit.setSelection(0, nickName.length());
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

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }
}
