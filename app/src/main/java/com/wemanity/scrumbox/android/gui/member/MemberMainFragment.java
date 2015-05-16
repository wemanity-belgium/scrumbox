package com.wemanity.scrumbox.android.gui.member;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.gui.member.dialog.MemberEditDialog;

import java.util.List;

import roboguice.inject.InjectResource;
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

    @InjectView(R.id.memberMainLayout)
    private RelativeLayout memberLayout;

    @InjectResource(R.drawable.default_profile_avatar)
    private Drawable defaultProfileAvatar;

    private MemberSwipeAdapter memberAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberAdapter = new MemberSwipeAdapter(getActivity());
        memberAdapter.setOnEntityChangeListener(new OnEntityChangeListener<Member>() {
            @Override
            public void onEntityChange(EntityAction action, Member entity) {
                switch (action){
                    case UPDATE:
                        showEditDialog(entity);
                        break;
                    case DELETE:
                        memberAdapter.remove(entity);
                        memberDao.delete(entity);
                        break;
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        memberAdapter.clear();
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

        memberAdapter.setMode(Attributes.Mode.Single);
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(memberListView.getChildAt(position - memberListView.getFirstVisiblePosition()))).open(true);
            }
        });
        memberListView.setAdapter(memberAdapter);
        memberAdapter.addAll(members);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addMemberFAB:
                showEditDialog(null);
                break;
        }
    }

    @Override
    public void onEntityChange(EntityAction action, Member entity) {
        switch (action){
            case INSERT:
                memberAdapter.add(entity);
                break;
            case UPDATE:
                memberAdapter.switchObjectById(entity);
                break;
        }
    }

    private void showEditDialog(Member member){
        MemberEditDialog memberDialog = MemberEditDialog.newInstance(member);
        memberDialog.setShowsDialog(false);
        memberDialog.setOnEntityChangeListener(this);
        memberDialog.show(getActivity().getFragmentManager(),"editMemberDialog");
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }
}