package com.wemanity.scrumbox.android.gui.member.dialog;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;

public class DialogCallBack extends MaterialDialog.ButtonCallback {

    @Inject
    private MemberDao memberDao;

    private Member member;

    public DialogCallBack() {
    }

    public DialogCallBack(Member member) {
        this.member = member;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        //MemberEditDialog memberDialog = (MemberEditDialog) dialog;
    }
}
