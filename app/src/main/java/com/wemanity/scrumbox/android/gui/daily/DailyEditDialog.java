package com.wemanity.scrumbox.android.gui.daily;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.DailyDao;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.dao.impl.ParticipantDao;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.db.entity.Participant;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.ViewUtils;
import com.wemanity.scrumbox.android.gui.base.AbstractEntityEditDialog;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.base.adapter.select.EntitySelectionAdapter;
import com.wemanity.scrumbox.android.util.StringUtils;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DailyEditDialog extends AbstractEntityEditDialog<Daily>{
    @Inject
    private MemberDao memberDao;

    @Inject
    private ParticipantDao participantDao;

    @Inject
    private DailyDao dailyDao;

    @InjectView(R.id.dailyParticipantListView)
    private ListView dailyparticipantListView;

    @InjectView(R.id.durationEditText)
    private EditText durationEditText;

    @InjectView(R.id.titleEditText)
    private EditText titleEditText;

    @InjectResource(R.string.daily_insert)
    private String insertTitle;

    @InjectResource(R.string.daily_update)
    private String updateTitle;


    public static DailyEditDialog newInstance(){
        return new DailyEditDialog();
    }

    public static DailyEditDialog newInstance(Daily daily){
        DailyEditDialog frag = new DailyEditDialog();
        Bundle args = new Bundle();
        args.putSerializable("entity", daily);
        frag.setArguments(args);
        return frag;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.new_daily_dialog;
    }

    @Override
    protected String getTitle(boolean insertMode, Daily entity) {
        return insertMode? insertTitle : String.format(updateTitle, entity.getTitle());
    }

    @Override
    protected void initializeView(View view, Bundle savedInstanceState) {
        List<Member> members = memberDao.queryBuilder().list();
        dailyparticipantListView.setAdapter(new EntitySelectionAdapter<>(this.getActivity(),members,R.layout.member_listview_row_layout, new int[]{R.id.memberNickNameTextView}, new String[]{"nickname"}));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void bindEntity(View view, Bundle savedInstanceState, Daily entity) {
        titleEditText.setText(entity.getTitle());
        durationEditText.setText(String.valueOf(entity.getDurationbyparticipant()));
        List<Member> selectedMembers = new ArrayList<>();
        for (Participant participant : entity.getParticipants()){
            selectedMembers.add(participant.getMember());
        }
        EntitySelectionAdapter<Member> adapter = (EntitySelectionAdapter)dailyparticipantListView.getAdapter();
        adapter.addSelectedEntities(selectedMembers);
    }


    @Override
    protected boolean valideInput(){
        if(!ViewUtils.checkEditText(titleEditText,getString(R.string.error_is_empty))){
            return false;
        }
        if(!ViewUtils.checkEditText(durationEditText,getString(R.string.error_is_empty))){
            return false;
        }
        if (((EntitySelectionAdapter)dailyparticipantListView.getAdapter()).getSelectedEntities().isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Daily insert(){
        Daily daily = new Daily();
        daily.setDurationbyparticipant(Integer.valueOf(StringUtils.clear(durationEditText)));
        daily.setTitle(titleEditText.getText().toString());
        dailyDao.insert(daily);
        Set<Member> members = ((EntitySelectionAdapter)dailyparticipantListView.getAdapter()).getSelectedEntities();
        for (Member member : members){
            Participant participant = new Participant();
            participant.setMember(member);
            participant.setDaily(daily);
            participantDao.insert(participant);
        }
        return daily;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void update(Daily entity){
        entity.setDurationbyparticipant(Integer.valueOf(StringUtils.clear(durationEditText)));
        entity.setTitle(titleEditText.getText().toString());
        Set<Member> members = new HashSet(((EntitySelectionAdapter)dailyparticipantListView.getAdapter()).getSelectedEntities());
        List<Participant> participants = entity.getParticipants();
        Iterator<Participant> participantIterator = participants.iterator();
        while (participantIterator.hasNext()){
            Participant participant = participantIterator.next();
            if (!members.contains(participant.getMember())){
                participant.delete();
                participantIterator.remove();
            } else {
                members.remove(participant.getMember());
            }
        }
        entity.update();
        for (Member member : members){
            Participant participant = new Participant();
            participant.setMember(member);
            participant.setDaily(entity);
            participantDao.insert(participant);
        }
        entity.resetParticipants();

    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }
}