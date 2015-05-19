package com.wemanity.scrumbox.android.gui.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.ParticipantDao;
import com.wemanity.scrumbox.android.db.dao.impl.DailyDao;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.db.entity.Participant;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.util.StringUtils;

import java.util.List;

import roboguice.fragment.provided.RoboDialogFragment;
import roboguice.inject.InjectView;

public class DailyEditDialog extends RoboDialogFragment implements View.OnClickListener{
    @Inject
    private MemberDao memberDao;

    @Inject
    private ParticipantDao participantDao;

    @Inject
    private DailyDao dailyDao;

    @InjectView(R.id.dailyParticipantListView)
    private ListView dailyparticipantListView;

    @InjectView(R.id.negativeButton)
    private TextView cancelButton;

    @InjectView(R.id.positiveButton)
    private TextView saveButton;

    @InjectView(R.id.durationEditText)
    private EditText durationEditText;

    @InjectView(R.id.titleEditText)
    private EditText titleEditText;

    private OnEntityChangeListener<Daily> onEntityChangeListener;

    private boolean insertMode;


    private Daily daily;

    public static DailyEditDialog newInstance(){
        DailyEditDialog frag = new DailyEditDialog();
        return frag;
    }

    public static DailyEditDialog newInstance(Daily daily){
        DailyEditDialog frag = new DailyEditDialog();
        Bundle args = new Bundle();
        args.putSerializable("daily", daily);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("daily")) {
            daily = (Daily) args.get("daily");
        }
        insertMode = (daily == null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_daily_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Member> members = memberDao.queryBuilder().list();
        if (daily == null){
            dailyparticipantListView.setAdapter(new MemberSelectionAdapter(this.getActivity(),members,R.layout.participant_listview_row_view, R.id.memberParticipationCheckBox));
        } else {
            titleEditText.setText(daily.getTitle());
            durationEditText.setText(daily.getDurationbyparticipant());
            dailyparticipantListView.setAdapter(new MemberSelectionAdapter(this.getActivity(),members,R.layout.participant_listview_row_view, R.id.memberParticipationCheckBox, daily.getParticipants()));
        }
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positiveButton:
                if (valideInput()){
                    if (insertMode) {
                        insertDaily();
                    } else{
                        upateDaily();
                    }
                    dismiss();
                }
                break;
            case R.id.negativeButton:
                dismiss();
        }
    }

    private boolean valideInput(){
        return !((MemberSelectionAdapter)dailyparticipantListView.getAdapter()).getMemberParticipent().isEmpty() &&
               !StringUtils.clear(durationEditText).isEmpty() &&
               !StringUtils.clear(titleEditText).isEmpty();
    }

    private void insertDaily(){
        daily = new Daily();
        daily.setDurationbyparticipant(new Integer(StringUtils.clear(durationEditText)));
        daily.setTitle(titleEditText.getText().toString());
        dailyDao.insert(daily);
        List<Member> members = ((MemberSelectionAdapter)dailyparticipantListView.getAdapter()).getMemberParticipent();
        for (Member member : members){
            Participant participant = new Participant();
            participant.setMember(member);
            participant.setDaily(daily);
            participantDao.insert(participant);
        }
        notifyEntityChangeListener();
    }

    private void upateDaily(){
        daily.setDurationbyparticipant(new Integer(StringUtils.clear(durationEditText)));
        daily.setTitle(titleEditText.getText().toString());
        List<Member> members = ((MemberSelectionAdapter)dailyparticipantListView.getAdapter()).getMemberParticipent();
        List<Participant> participants = daily.getParticipants();
        for(Participant participant : participants){
            if (!members.contains(participant.getMember())){
                participant.delete();
                members.remove(participant.getMember());
            }
        }
        daily.update();
        for (Member member : members){
            Participant participant = new Participant();
            participant.setMember(member);
            participant.setDaily(daily);
            participantDao.insert(participant);
        }
    }

    private void notifyEntityChangeListener(){
        if (onEntityChangeListener != null){
            onEntityChangeListener.onEntityChange(insertMode? EntityAction.INSERT : EntityAction.UPDATE, daily);
        }
    }

    public OnEntityChangeListener<Daily> getOnEntityChangeListener() {
        return onEntityChangeListener;
    }

    public void setOnEntityChangeListener(OnEntityChangeListener<Daily> onEntityChangeListener) {
        this.onEntityChangeListener = onEntityChangeListener;
    }
}