package com.wemanity.scrumbox.android.gui.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.FragmentHelper;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.SubMenuOnScrollListener;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.gui.base.adapter.SubMenuAdapter;
import com.wemanity.scrumbox.android.gui.daily.DailyEditDialog;

import java.util.List;

import roboguice.inject.InjectView;

public class MemberMainFragment extends BaseFragment implements View.OnClickListener,
        OnEntityChangeListener<Member> {
    public static final String TAG = "DailyMainFragment";

    @Inject
    private MemberDao memberDao;

    @InjectView(R.id.memberListView)
    private ListView memberListView;

    @InjectView(R.id.addMemberFAB)
    private FloatingActionButton memberFAB;

    private SubMenuAdapter<Member> entityAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.member_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memberFAB.setOnClickListener(this);
        List<Member> members = memberDao.queryBuilder().list();
        /*members.add(new Member("Member 1"));
        members.add(new Member("Member 2"));
        members.add(new Member("Member 3"));
        members.add(new Member("Member 4"));
        members.add(new Member("Member 5"));
        members.add(new Member("Member 6"));
        members.add(new Member("Member 7"));*/
        entityAdapter = SubMenuAdapter.<Member>newBuilder()
                .drawables(new int[]{R.drawable.member_delete,
                        R.drawable.member_edit})
                .entityActions(new EntityAction[]{EntityAction.DELETE, EntityAction.SHOW_EDIT_DIALOG})
                .activity(getActivity())
                .layoutId(R.layout.member_listview_row_layout)
                .entities(members)
                .viewIds(new int[]{R.id.memberNickNameTextView})
                .properties(new String[]{"nickname"})
                .changeListener(this)
                .build();
        memberListView.setAdapter(entityAdapter);
        memberListView.setOnScrollListener(new SubMenuOnScrollListener(entityAdapter));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addMemberFAB:
                showMemberEditDialog();
                break;
        }
    }

    @Override
    public void onEntityChange(EntityAction action, Member entity) {
        switch (action){
            case CREATE:
                entityAdapter.add(entity);
                break;
            case SHOW_CREATION_DIALOG:
            case SHOW_EDIT_DIALOG:
                showMemberEditDialog(entity);
                break;
            case DELETE:
                entityAdapter.remove(entity);
                memberDao.delete(entity);
                break;
            default:
                throw new RuntimeException("Action not implemented yet : "+ action);
        }
    }

    private void showMemberEditDialog(Member member){
        //MemberEditDialog memberDialog = MemberEditDialog.newInstance(member);
        //memberDialog.setShowsDialog(false);
        //memberDialog.setOnEntityChangeListener(this);
        Bundle args = new Bundle();
        args.putSerializable("entity", member);
        FragmentHelper.switchFragment(getActivity(), MemberEditDialog.class, R.id.fragmentFrameLayout, args);
        //getChildFragmentManager().beginTransaction().add(memberDialog, "EditMember").commit();
        //memberDialog.show(getActivity().getFragmentManager(),"editMemberDialog");
    }

    private void showMemberEditDialog(){
        showMemberEditDialog(null);
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }
}